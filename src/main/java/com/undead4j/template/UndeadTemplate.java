package com.undead4j.template;

import com.undead4j.js.JS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UndeadTemplate {
  static final Map<String, String> ENTITIES =
      Map.of(
          "&", "&amp;",
          "<", "&lt;",
          ">", "&gt;",
          "\"", "&quot;",
          "'", "&#39;",
          "/", "&#x2F;",
          "`", "&#x60;",
          "=", "&#x3D;");
  static final Pattern ENT_REGEX = Pattern.compile(String.join("|", ENTITIES.keySet()));
  private final StringTemplate raw;

  public UndeadTemplate(StringTemplate template) {
    this.raw = template;
  }


  static final String escapeStringHTML(String input) {
    var m = ENT_REGEX.matcher(input);
    return m.replaceAll(
        (replacer) -> {
          // replace matches with correct escaped entity
          return ENTITIES.get(replacer.group());
        });
  }

  static final String escapeHTML(Object input) {
    switch (input) {
      case null -> {
        return "";
      }
      case UndeadTemplate t -> {
        return t.toString();
      }
      case JS js -> {
        return escapeHTML(js.toJSON());
      }
      case List l -> {
        // get first element of list to determine type
        if (l.size() == 0) {
          return "";
        }
        var first = l.get(0);
        if(first instanceof UndeadTemplate) {
          // concatenate all templates
          return concat((List<UndeadTemplate>) l).toString();
        }
        else if(first instanceof String) {
          // concatenate all strings
          return (String)l.stream().map(i -> String.valueOf(i)).collect(Collectors.joining());
        }
        else {
          throw new RuntimeException("only hande arrays of UndeadTemplates or Strings" + input);
        }
      }
      case String s -> {
        return escapeStringHTML(s);
      }
      case Number n -> {
        return escapeStringHTML(String.valueOf(n));
      }
      case Boolean b -> {
        return escapeStringHTML(String.valueOf(b));
      }
      default -> {
        throw new RuntimeException("Expected type" + input);
      }
    }
  }

  /**
   * concat concatenates multiple templates into a single template
   * @param tmpls templates to concatenate
   * @return a new template that is the concatenation of all templates
   */
  public static UndeadTemplate concat(UndeadTemplate... tmpls) {
    // return the single template if there is only one
    if(tmpls.length == 1) {
      return tmpls[0];
    }
    // build new fragments and values
    var fragments = new ArrayList<String>();
    var values = new ArrayList<>();
    for (var tmpl : tmpls) {
      // templates must have one more fragment than values so when we concatenate templates
      // we must merge the last fragment of the previous template with the first fragment of
      // the next template and then add the rest of the fragments and values
      if(fragments.size() > 0) {
        // concat last fragment of previous template with first fragment of next template
        var lastFrag = fragments.get(fragments.size() - 1);
        var nextFrag = tmpl.raw.fragments().get(0);
        fragments.set(fragments.size() - 1, lastFrag + nextFrag);
        // now add the rest of the fragments from the next template
        fragments.addAll(tmpl.raw.fragments().subList(1, tmpl.raw.fragments().size()));
      } else {
        // this must be the first template so just add all fragments
        fragments.addAll(tmpl.raw.fragments());
      }
      // add all values regardless of if this is the first template or not
      values.addAll(tmpl.raw.values());
    }
    return new UndeadTemplate(StringTemplate.of(fragments, values));
  }

  public static UndeadTemplate concat(Collection<UndeadTemplate> tmpls) {
    return concat(tmpls.toArray(UndeadTemplate[]::new));
  }

  public UndeadTemplate concatWith(UndeadTemplate other) {
    return concat(this, other);
  }

  /**
   * trim removes whitespace from front and back of template returning a new template
   */
  public UndeadTemplate trim() {
    var fragments = new ArrayList<String>();
    for (var i = 0; i < this.raw.fragments().size(); i++) {
      var fragment = this.raw.fragments().get(i);
      if (i == 0) {
        fragment = fragment.stripLeading();
      }
      if (i == this.raw.fragments().size() - 1) {
        fragment = fragment.stripTrailing();
      }
      fragments.add(fragment);
    }
    return new UndeadTemplate(StringTemplate.of(fragments, this.raw.values()));
  }

  public Map<String, Object> toParts() {
    var indexedValues =
        IntStream.range(0, this.raw.values().size())
            .mapToObj(
                i -> {
                  // TODO handle all case where type of value(i)
                  var item = this.raw.values().get(i);
                  Object val;
                  switch (item) {
                    case null -> {
                      val = "";
                    }
                    case UndeadTemplate tmpl -> {
                      // if there is a single fragment in child template then we can
                      // just use that directly instead of full parts tree
                      if (tmpl.raw.fragments().size() == 1) {
                        val = tmpl.raw.fragments().get(0);
                      } else {
                        // recurse into child template
                        val = tmpl.toParts();
                      }
                    }
                    case JS js -> {
                      // TODO peek if in attribute with single or double quotes and if single then don't escape
                      val = escapeHTML(js);
                    }
                    case ArrayList arr -> {
                      throw new RuntimeException("Implement array list" + arr);
                    }
                    case String s -> {
                      val = escapeHTML(s);
                    }
                    case Number n -> {
                      val = escapeHTML(String.valueOf(n));
                    }
                    default -> {
                      throw new RuntimeException("Expected type in LiveTemplate:" + item);
                    }
                  }
                  return Map.entry(String.valueOf(i), val);
                })
            .collect(Collectors.toMap(i -> i.getKey(), i -> i.getValue()));
    // add statics
    indexedValues.put("s", raw.fragments());
    return indexedValues;
  }

  @Override
  public String toString() {
    var newValues = new ArrayList<>();

    for (Object value : this.raw.values()) {
      newValues.add(escapeHTML(value));
    }

    return StringTemplate.interpolate(this.raw.fragments(), newValues);
  }

  public UndeadTemplate noEsc() {
    return this;
  }

}

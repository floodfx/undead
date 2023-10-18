package com.undead4j.template;

import java.util.ArrayList;
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
      case ArrayList a -> {
        throw new RuntimeException("Handle Array");
      }
      case String s -> {
        return escapeStringHTML(s);
      }
      case Number n -> {
        return escapeStringHTML(String.valueOf(n));
      }
      default -> {
        throw new RuntimeException("Expected type" + input);
      }
    }
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

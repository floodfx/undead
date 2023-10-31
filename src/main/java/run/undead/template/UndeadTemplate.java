package run.undead.template;

import run.undead.js.JS;
import run.undead.form.Form;
import run.undead.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * UndeadTemplate is a {@link StringTemplate} based template engine that is designed to work with
 * Undead (i.e. the LiveView protocol) and is used by Undead {@link View}s to
 * define the HTML that will be rendered to the client from {@link View#render}.
 * UndeadTemplate automatically escapes HTML entities in the "dynamic" parts of the template to mitigate XSS attacks.
 *
 *  <p>
 *   To create an UndeadTemplate you use the {@link Directive#HTML} static {@link StringTemplate.Processor} method.
 *   For example:
 *   <pre>{@code
 *     // single line template
 *     UndeadTemplate tmpl = Undead.HTML."Hello \{ name }";
 *     // or multi-line template
 *     UndeadTemplate tmpl = Undead.HTML."""
 *        Hello \{ name }
 *     """;
 *   }</pre>
 *   {@link StringTemplate}s use the <code>\{}</code> syntax to denote embedded expressions. These embedded expressions
 *   are the dynamic parts of the template and are evaluated automatically by Undead when the template needs to be
 *   rendered.
 *  </p>
 *  <p>
 *   You can of course embed HTML within the template as well.  For example:
 *   <pre>{@code
 *     UndeadTemplate tmpl = Undead.HTML."<div class="text-2xl">Hello \{ name }</div>";
 *   }</pre>
 *  </p>
 *  <p>
 *    Undead provides a number of template directive helpers that assist in concisely defining templates that require
 *    conditional logic, loops, etc.  For example:
 *    <pre>{@code
 *      UndeadTemplate tmpl = Undead.HTML."""
 *      <div class="text-2xl">Hello \{ When(name != null), name, HTML."World" }</div>
 *      """;
 *    }</pre>
 *    The following template directives are available (see {@link Directive} for more information):
 *    <ul>
 *      <li>{@link Directive#If} - show an {@link UndeadTemplate} based on conditional logic</li>
 *      <li>{@link Directive#Range} - iterate over a range of ints</li>
 *      <li>{@link Directive#Switch} - display specific {@link UndeadTemplate} based on value</li>
 *      <li>{@link Directive#Map} - map a collection to an {@link UndeadTemplate}</li>
 *      <li>{@link Directive#Join} - concatenate a collection of {@link UndeadTemplate}s</li>
 *    </ul>
 *  </p>
 *  <p>
 *    You can easily define your own functions that allow you to build up and abstract complex template
 *    logic.  For example:
 *    <pre>{@code
 *      // define a function that returns a template based on the value of an input
 *      public static UndeadTemplate MaybeShowUser(User user) {
 *        if(user == null) {
 *          return Undead.EMPTY;
 *        }
 *        return Undead.HTML."""
 *          <div class="text-2xl">Hello \{ user.name() }</div>
 *        """;
 *      }
 *      // now you can use this function in your template
 *      UndeadTemplate tmpl = Undead.HTML."""
 *        <div>...</div>
 *        \{ MaybeShowUser(user) }"
 *        <div>...</div>
 *      """;
 *    }</pre>
 *  </p>
 *
 *  <h3>Client Event Bindings</h3>
 *  <p>
 *    In order to create a rich, dynamic user experience, Undead needs to receive events from the client about user
 *    interactions such as clicks, key-presses, focus events, etc.  In order to subscribe to these event, you add
 *    specific attributes (a.k.a "bindings") to the HTML elements in your template.  These bindings are prefixed with
 *    <code>ud-</code> and are automatically sent to the server when the event occurs.
 *    <br/>
 *    <strong>Note:</strong>When an event is sent to the server it is routed to the {@link View#handleEvent}
 *    method of the {@link View} that rendered the template. See {@link View} for more
 *    details.
 *
 *    Here is an example:
 *    <pre>{@code
 *      // send a "my-event" event to the server when the button is clicked
 *      UndeadTemplate tmpl = Undead.HTML."""
 *        <button ud-click="my-event">Click Me</button>
 *      """;
 *      }
 *     }</pre>
 *     Undead supports the following attributes (a.k.a bindings):
 *     <ul>
 *       <li><code>ud-click</code> - send named event to the server when a user clicks on this element</li>
 *       <li><code>ud-click-away</code> - send named event to the server when the user clicks outside from the element</li>
 *       <li><code>ud-keydown</code> - send named event to the server for keydown events in this element</li>
 *       <li><code>ud-window-keydown</code> - send named event to the server for keydown events in the window</li>
 *       <li><code>ud-keyup</code> - send named event to the server for keydown event this element</li>
 *       <li><code>ud-window-keyup</code> - send named event to the server for keyup events in the window</li>
 *       <li><code>ud-focus</code> - send named event to the server for focus events on this element</li>
 *       <li><code>ud-window-focus</code> - send named event to the server for focus events in the window</li>
 *       <li><code>ud-blur</code> - send named event to the server for blur events on this element</li>
 *       <li><code>ud-window-blur</code> - send named event to the server for blur events in the window</li>
 *     </ul>
 *   </p>
 *   <h4>Value Attribute</h4>
 *   <p>
 *     You can couple the above bindings with a <code>ud-value-KEY</code> (where <code>KEY</code> is the key of
 *     the value you want to send to the server when the event occurs.  For example:
 *     <pre>{@code
 *       // send a "my-event" event to the server when the button is clicked along with a key of "foo" and value of "bar"
 *       UndeadTemplate tmpl = Undead.HTML."""
 *         <button ud-click="my-event" ud-value-foo="bar">Click Me</button>
 *       """;
 *     }</pre>
 *
 *     This is useful for sending additional data to the server when the event occurs.
 *   </p>
 *
 *   <h4>Keydown and Keyup Events</h4>
 *   <p>
 *     The above keydown and keyup events can be further restricted by coupling them with a <code>ud-key</code> attribute which
 *     will restrict the event to only be sent when the key matches the value of the <code>ud-key</code> attribute.  For
 *     example:
 *     <pre>{@code
 *       // send a "my-event" event to the server when the user presses the "Slash" key
 *       UndeadTemplate tmpl = Undead.HTML."""
 *         <div ud-window-keydown="load_search" ud-key="Slash">...</div>
 *       """;
 *     }</pre>
 *
 *   </p>
 *
 *   <h4>Form Events</h4>
 *   <p>
 *     Two of the most common events that you will want to handle are form changes and form submissions.  Undead
 *     provides form specific attribute bindings for these events that make it easy to handle them.  For example:
 *     <pre>{@code
 *       // send a "validate" event to the server when the form changes
 *       // and send a "submit" event to the server when the form is submitted
 *       UndeadTemplate tmpl = Undead.HTML."""
 *         <form ud-change="validate" ud-submit="save">
 *           <input type="text" name="name" />
 *           <input type="text" name="email" />
 *           <button type="submit">Submit</button>
 *         </form>
 *       """;
 *     }</pre>
 *
 *     The code above will send a <code>validate</code> event to the server when any inut in the form changes and
 *     a <code>save</code> event to the server when the form is submitted.  Validating and binding form data to a
 *     model is so common that Undead provides a {@link Form} class that makes it extremely easy
 *     to validate and map form data to Java classes.  See {@link Form} for more information.
 *   </p>
 *   <h4>Throttling / Debouncing</h4>
 *   <p>
 *     Undead supports throttling and debouncing of events as well.  This is useful for events that can occur frequently
 *     like input change events on a text input.  Instead of getting an event for every keypress, you can debounce
 *     form changes to only send an event to the server after a certain amount of time has passed since the last
 *     event.  For example:
 *     <pre>{@code
 *       // send a "validate" event to the server after 500ms has passed since the last change event
 *       UndeadTemplate tmpl = Undead.HTML."""
 *         <form ud-change="validate" ud-change="save">
 *           <input type="text" name="name" ud-debounce="500"/>
 *           <button type="submit">Submit</button>
 *         </form>
 *       """;
 *     }</pre>
 *
 *     You can also debounce on a blur event instead of duration.  For example:
 *     <pre>{@code
 *       // send a "validate" event to the server after the input loses focus
 *       UndeadTemplate tmpl = Undead.HTML."""
 *         <form ud-change="validate" ud-change="save">
 *           <input type="text" name="name" ud-debounce="blur"/>
 *           <button type="submit">Submit</button>
 *         </form>
 *       """;
 *     }</pre>
 *
 *     You can also throttle events by using the <code>ud-throttle</code> attribute.  For example:
 *     <pre>{@code
 *       // send a "volume_up" event to the server at most once every 500ms
 *       UndeadTemplate tmpl = Undead.HTML."""
 *         <button ud-click="volume_up" ud-throttle="500">Volume Up</button>
 *       """;
 *     }</pre>
 *   </p>
 *
 *  <h3>JS Commands</h3>
 *  <p>
 *    UndeadTemplate can contain {@link JS} which serialize down to javascript commands that execute on the client
 *    to provide more dynamic behavior.  These are completely optional but can be useful for css transitions,
 *    hiding/showing elements, and otherwise manipulating the DOM.
 *    See {@link JS} for more information.
 *  </p>
 */
public class UndeadTemplate {
  private static final Map<String, String> ENTITIES =
      Map.of(
          "&", "&amp;",
          "<", "&lt;",
          ">", "&gt;",
          "\"", "&quot;",
          "'", "&#39;",
          "/", "&#x2F;",
          "`", "&#x60;",
          "=", "&#x3D;");
  private static final Pattern ENT_REGEX = Pattern.compile(String.join("|", ENTITIES.keySet()));
  private final StringTemplate raw;

  public UndeadTemplate(StringTemplate template) {
    this.raw = template;
  }


  private static final String escapeStringHTML(String input) {
    var m = ENT_REGEX.matcher(input);
    return m.replaceAll(
        (replacer) -> {
          // replace matches with correct escaped entity
          return ENTITIES.get(replacer.group());
        });
  }

  private static final String escapeHTML(Object input) {
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
        // TODO throw error?
        // throw new RuntimeException("Unexpected type" + input.getClass() + " " + input);
        // for now be lenient and just use toString
        return escapeStringHTML(input.toString());
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

  /**
   * trim removes whitespace from front and back of template returning a new instance
   * of an UndeadTemplate
   * @return a new instance of an UndeadTemplate with whitespace removed from front and back
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

  /**
   * toParts returns the parts of the template as a Map of String to Object.  This is
   * used by Undead during Websocket rendering to send the parts of the template to the
   * client.
   * @return a Map of String to Object
   */
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

  /**
   * toString returns the HTML string representation of the template
   * @return the HTML string representation of the template
   */
  @Override
  public String toString() {
    var newValues = new ArrayList<>();

    for (Object value : this.raw.values()) {
      newValues.add(escapeHTML(value));
    }

    return StringTemplate.interpolate(this.raw.fragments(), newValues);
  }

  /**
   * noEsc returns the template without escaping the dynamic parts of the template. Be
   * careful when using this method as it can lead to XSS attacks if you do not properly
   * escape the dynamic parts of the template yourself.
   * @return the template without escaping the dynamic parts of the template
   */
  public UndeadTemplate noEsc() {
    return this;
  }

}

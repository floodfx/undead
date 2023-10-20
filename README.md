# 🧟 Undead - LiveViews for the JVM

## Who is this for?
Undead is for people with 🧠s who want to build dynamic, reactive front-end experiences on the JVM *without writing javascript*.

## What is Undead?
Undead is a LiveView implementation for the JVM. (The name "Undead" is a hat tip to the "Live" part of LiveViews 👻.)
LiveViews let you build front-end experiences like those you can build with [React](https://react.dev) or [Vue.js](https://vuejs.org) 
but all without leaving the server.  

At a high level, a LiveView is a server process that receives events (clicks, form input, etc) from the browser, updates its state, 
and sends back diffs which are applied to the browser.  Undead makes it easy to build LiveViews by automatically routing events, 
providing developer hooks for updating state, and rendering (and diffing) the HTML, and applying those diffs efficiently so the developers
can just focus on achieving the desired user experience.

LiveViews where invented and popularized by the [Phoenix Framework](https://www.phoenixframework.org/) which is written in Elixir and
runs on the Erlang VM.  Obviously, Undead is not Elixir or Erlang but it adheres to the LiveView protocol and reuses the client-side
javascript from the Phoenix Framework.  (Suffice it to say, the Phoenix Framework is awesome and kudos to the Elixir/Erlang community for
inventing LiveViews.)

## 🍬 Advantages of Undead (and LiveViews in general)
 * No need to write javascript
 * Very fast, SEO-friendly, fully rendered HTML initial response
 * Extremely efficient, diff-based, updates over Websockets
 * Small, easy-to-learn, yet powerful API
 * Modern, type-safe templating engine
 * Just another route on your existing web server

## Example Undead View
```java
// Sample View that counts up and down 
public class UndeadCounter implements View {
  private int count;

  public UndeadCounter() {
    this.count = 0;
  }
  
  // Handle events from the browser
  public void handleEvent(Socket socket, UndeadEvent event) {
    if ("inc".equals(event.type())) {
      this.count++;
    } else if ("dec".equals(event.type())) {
      this.count--;
    }
  }

  // Render the HTML based on the current state (e.g. count)
  public UndeadTemplate render(Meta meta) {
    return HTML."""
      <div class="flex flex-col space-y-4 mx-4">
        <h2 class="text-2xl">Count:
          <span class="\{ When(count > 0, HTML."text-success", HTML."text-warning") }">
            \{ count }
          </span>
        </h2>
        <div class="flex space-x-4">
          <button class="btn btn-primary" type="button" phx-click="dec">Decrement</button>
          <button class="btn btn-primary" type="button" phx-click="inc">Increment</button>
        </div>
        """;
  }

}
```

## Templating
Undead provides a [String Templates](https://openjdk.java.net/jeps/430)-based template engine that make LiveViews possible.  
String Templates are a new feature in Java 21 and therefore Undead **only works on Java 21** (and theoretically above).  String
Templates have a slightly weird escape syntax (e.g. `\{ count }`) but they are very powerful, easy to use, type-safe, and can 
create whatever HTML you want.  Undead provides a number of pre-built directives that make building complex HTML easy.

### Example Undead Template
```java
Undead.HTML."""
<div class="flex flex-col space-y-4 mx-4">
  <h2 class="text-2xl">Count:
    <span class="\{ When(count > 0, HTML."text-success", HTML."text-warning") }">
      \{ count }
    </span>
  </h2>
  <div class="flex space-x-4">
    <button class="btn btn-primary" type="button" phx-click="dec">Decrement</button>
    <button class="btn btn-primary" type="button" phx-click="inc">Increment</button>
  </div>
  """;
```
### `Undead.HTML` Templates
The `Undead.HTML` processor is a [String Template Processor](https://openjdk.java.net/jeps/430) that is used to build
HTML template in a type-safe, LiveView-friendly way.  `Undead.HTML` automatically escapes HTML entities in order to prevent XSS attacks.  Beyond escaping HTML
entities, it does no other parsing or validation of the HTML but it does take advantage of StringTemplates to optimize diff being sent over the wire.  
A simple example of `Undead.HTML` in action is below:
```java
// Single line
Undead.HTML."Hello, \{ name }!";

// Multiline
Undead.HTML."""
  <div class="flex flex-col space-y-4 mx-4">
    <h2 class="text-2xl">Hello, \{ name }!</h2>      
  </div>
""";
```
You can read more about String Templates [here](https://openjdk.java.net/jeps/430) and [here](https://www.infoq.com/news/2023/04/java-gets-a-boost-with-string/).  But you really don't
need to know much about them to use `Undead.HTML` templates other than how to embed values (i.e. `\{ myValue }`).  Note, you can statically import `Undead.HTML` to shorten it to `HTML`.

### Undead Template Directives
Undead provides template directives for common needs like if statements, switch statements, mapping over a collection, ranges, etc.  Basically all the normal
templating things you'd expect. You can also easily add your own template directives and/or custom functions that can be used in your templates.  Below is a list
of the built-in directives:
 
 * `If` Directive - `If` models an if statement in a template.  If the provided condition is true the trueCase template is returned 
otherwise an empty template is returned.
 * `When` Directive - `When` models a ternary operator in a template.  If the provided condition is true the trueCase template is returned
otherwise the falseCase template is returned.
 * `Switch` Directive - `Switch` models a switch statement in a template.  The provided value is compared to each case and if there is a match
the corresponding template is returned.  If there is no match, the default template is returned (or an empty template if there is no default).
 * `Map` Directive - `Map` enables transforming a collection of values into a collection of templates.  The provided collection is iterated over
and the provided template function is applied to each value in the collection.
 * `Range` Directive - `Range` enables iterating over a range of integer values optionally by a given step.
 * `Join` Directive - `Join` enabled joining a collection of templates together with a given separator template.

There are typically two types of logic directive, those that take a simple boolean condition (e.g. x == 1) and 
those that take a `Predicate`s which are used into lambda form (e.g. x -> x == 1).  The former is more concise but the latter is more flexible.  
Additionally, the directives that accept a `Predicate` also require a `Function<T, UndeadTemplate>` to be provided.  This means the function that
returns the template has access to the value being tested which is very useful for adding to a template or using `Function` defined elsewhere.

Examples
```java
// If Directive
HTML."""
  \{ If(zombieCount > 0, HTML."<span>🙀Uh oh, there are zombies!</span>") }
  """;

// When Directive
HTML."""
  <div>\{ 
    When(brainsEaten < 10, 
      HTML."Need more 🧠s...", 
      HTML."Time for 🛏!") 
    }
  </div>
  """;

// Switch Directive
HTML."""
  <div class="\{
    Switch(
      Case.of("blue".equals(color), HTML."text-blue"),
      Case.of("red".equals(color), HTML."text-red"),
      Case.defaultOf(HTML."text-green")
    )}">
    \{ color } is a nice color
  </div>
""";

// Map Directive
HTML."""
  <ul>
    \{ Map(zombies, zombie -> HTML."<li>\{ zombie.name }</li>") }
  </ul>
""";

// Range Directive
HTML."""
  \{Map(
      Range(10),
      i -> HTML."<div>Step \{i}</div>"
  )}
""";
// Range Directive with step
HTML."""
  \{Map(
      Range(2, 10, 2),
      i -> HTML."<div>\{i}</div>"
  )}
"""

```

### Using Undead Templates for "DeadView" (i.e. traditional) HTML rendering
It is fine to use Undead Templates to render HTML outside of LiveViews so feel free to do so. You can use the `Undead.HTML` processor
the same way and call `toString()` on it to get the rendered HTML.

## Why did you write Undead?
LiveViews (a.k.a UndeadViews 🧟) are an awesome, new paradigm for building dynamic front-end experience and I wanted to see if how hard it would be to
implement them on the JVM.  I poked around the existing templating libraries and didn't find anything that was ideal but when 
I learned about String Templates I thought they might be a good fit and waited for them to be released.  Once they were released,
it was just a matter of getting the rust off my Java skills (still very rusty!).  

It also helps that I've written (or co-written) LiveView libraries for [Javascript](https://liveviewjs) and [Go](https://github.com/canopyclimate/golive) so know the protocol fairly well
at this point.  

## Status
Most of these are advanced features or internal details that you don't need to know about to use Undead.
~~This is very early / PoC stage / "nothing works".~~ The basics work:
 - [x] Templating - `Undead.HTML`, `If`, `When`, `Switch`, `Map`, `Range`, `Join` Directives
 - [x] Message JSON parsing and generation
 - [x] Basic Protocol `phx-join`, `heartbeat`, page events (`click`, `keyup`, `blur`,  etc) and `form` events
 - [x] Internal Events (i.e. InfoEvents)
 - [x] Example Project - Javalin + Undead with examples: `UndeadCounter`, `UndeadUserForm`, `UndeadSalesDashnoard`
 - [ ] Parts Diffing
 - [ ] Push Events
 - [ ] Live Patch / Navigation
 - [ ] File Uploads
 - [ ] UndeadComponents (i.e. LiveComponents)
 - [ ] Altering Client Javascript


## Feedback Welcome
I haven't written Java professionally in about 12 years so would love any feedback and/or PRs!

## Elixir / Erlang folks
Please don't [Greenspun's tenth rule](https://en.wikipedia.org/wiki/Greenspun%27s_tenth_rule) me about "the BEAM" 🙀😀.
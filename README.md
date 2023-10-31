# 🧟🧛‍♀️🐺 Undead - LiveViews for the JVM

## What is Undead?

`Undead` is dead-serious library for JVM devs who want to build scary fast, bewitching front-end experiences on the JVM
*without writing javascript* 🙀.

`Undead` is a LiveView implementation for the JVM. (The name "Undead" is a play on the "Live" part of LiveViews 👻.)
LiveViews let you build front-end experiences like those you can build with [React](https://react.dev)
or [Vue.js](https://vuejs.org)
but all without leaving the server.

At a high level, a Live...err... an UndeadView, is a server process that receives events (clicks, form input, etc) from
the browser, updates
its state, and sends back diffs which are applied to the browser. You could say it makes "spooky action at a
distance"  very easy for developers ✨. `Undead` handles all the spine-chillingly difficult things; automatically routing
events, providing a clean API for devs, and rendering (and diffing) the HTML, and applying those diffs efficiently so
the
developers can just focus on enchanting their users.

LiveViews where invented and popularized by the [Phoenix Framework](https://www.phoenixframework.org/) which is written
in Elixir and
runs on the Erlang VM. Obviously, `Undead` is not Elixir or Erlang but it adheres to the LiveView protocol and reuses
the client-side
javascript from the Phoenix Framework.  (Suffice it to say, the Phoenix Framework is awesome and kudos to the
Elixir/Erlang community for
inventing LiveViews! 🙌)

## 🍬 Treats of Undead (and LiveViews in general)

* No need to write javascript
* Very fast, SEO-friendly, fully rendered HTML initial response
* Extremely efficient, diff-based updates over Websockets
* Small, easy-to-learn, yet powerful API
* Modern, type-safe templating engine (String Templates)
* Just another route on your existing web server

## 🦠 Current Status: Incubating / Alpha

`Undead` is currently in the "incubating" stage. It is probably not yet ready for production but "it's alive!🧟" and
a decent chunk of functionality is implemented along with pretty good documentation (especially the javadocs). If you
aren't too afraid 👻 you can try it out and provide feedback. Check out working examples in
the [example](src/main/java/run/undead/javalin/example)
code directory. You can run these examples by checking out this repository and running:

```shell
mvn package exec:exec
```

Then open your browser to http://localhost:1313 and you should see the examples.

## ⭐️ Stars, Feedback, and Signups Welcome

Please feel free to open an issue or PR if you have any feedback or suggestions. If you like this project, feel free
to ⭐️ it!  You can also [signup for updates](https://mailchi.mp/40717440e95b/undead-signup).  (Only the cursed would
share your email address.)

## Adding `Undead` to your project

If you are brave enough to try it out, you can add `Undead` to your project by adding the following dependency to your
`pom.xml`:

```xml

<dependency>
    <groupId>run.undead</groupId>
    <artifactId>undead-core</artifactId>
    <version>0.0.13</version>
</dependency>
```

## Example Undead View

```java
// Sample View that counts up and down 
public class UndeadCounter implements View {
  private int count;

  public UndeadCounter() {
    this.count = 0;
  }

  // Handle events from the browser
  public void handleEvent(Socket context, UndeadEvent event) {
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
            <span class="\{ If(count > 0, HTML."text-success", HTML."text-warning") }">
              \{ count }
            </span>
          </h2>
          <div class="flex space-x-4">
            <button class="btn btn-primary" type="button" ud-click="dec">Decrement</button>
            <button class="btn btn-primary" type="button" ud-click="inc">Increment</button>
          </div>
          """;
  }

}
```

## Undead Views

Undead `View`s are the lifeblood of the Undead framework.  `View` provides a simple yet powerful interface that for
handling events from the browser, updating state, and rendering HTML.

### `Undead` View interface

The View interface defines the lifecycle callbacks for an Undead View. Views have a lifecycle that consists of a short,
fast HTTP request/response and a long-lived WebSocket connection.

**Implementation Note:** Views should have a no-arg constructor as they are instantiated by reflection.

#### View Interface methods

- `View.mount` - (optional) mount is where a View can be initialized based on session data and/or path or query
  parameters
- `View.handleParams` - (optional) handleParams is called after mount and whenever there is a URI change
- `View.handleEvent` - (optional) handleEvent is called when an event (click, keypress, etc) is received from the
  browser
- `View.handleInfo` - (optional) handleInfo is called when an event (a.k.a info) is received from the server
- `View.render` - (required) render returns an `UndeadTemplate` based on the state of the View
- `View.shutdown` - (optional) shutdown is called when the View is shutting down and is a good place to clean up

As you can see the only required method is `View.render` which is called to render the View based on its state. The
other methods are optional so you can implement only the callbacks that you need for your View.

## Templating

Undead provides a [String Templates](https://openjdk.java.net/jeps/430)-based template engine that make LiveViews
possible.  
String Templates are a new feature in Java 21 and therefore Undead **only works on Java 21** (and theoretically above).
String
Templates have a slightly weird escape syntax (e.g. `\{ count }`) but they are very powerful, easy to use, type-safe,
and can
create whatever HTML you want. Undead provides a number of pre-built directives that make building complex HTML easy.

### Example Undead Template

```java
Undead.HTML."""
<div class="flex flex-col space-y-4 mx-4">
  <h2 class="text-2xl">Count:
    <span class="\{ If(count > 0, HTML."text-success", HTML."text-warning") }">
      \{ count }
    </span>
  </h2>
  <div class="flex space-x-4">
    <button class="btn btn-primary" type="button" ud-click="dec">Decrement</button>
    <button class="btn btn-primary" type="button" ud-click="inc">Increment</button>
  </div>
  """;
```

### `Undead.HTML` Templates

The `Undead.HTML` processor is a [String Template Processor](https://openjdk.java.net/jeps/430) that is used to build
HTML template in a type-safe, LiveView-friendly way.  `Undead.HTML` automatically escapes HTML entities in order to
prevent XSS attacks. Beyond escaping HTML
entities, it does no other parsing or validation of the HTML but it does take advantage of StringTemplates to optimize
diff being sent over the wire.  
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

You can read more about String Templates [here](https://openjdk.java.net/jeps/430)
and [here](https://www.infoq.com/news/2023/04/java-gets-a-boost-with-string/). But you really don't
need to know much about them to use `Undead.HTML` templates other than how to embed values (i.e. `\{ myValue }`). Note,
you can statically import `Undead.HTML` to shorten it to `HTML`.

### Undead Template Directives

Undead provides template directives for common needs like if statements, switch statements, mapping over a collection,
ranges, etc. Basically all the normal
templating things you'd expect. You can also easily add your own template directives and/or custom functions that can be
used in your templates. Below is a list
of the built-in directives:

* `If` Directive - `If` models an if statement in a template. If the provided condition is true the trueCase template is
  returned
  otherwise an empty template is returned. There is also an `If` directive that can return either the trueCase or
  falseCase template.
* `Switch` Directive - `Switch` models a switch statement in a template. The provided value is compared to each case and
  if there is a match
  the corresponding template is returned. If there is no match, the default template is returned (or an empty template
  if there is no default).
* `Map` Directive - `Map` enables transforming a collection of values into a collection of templates. The provided
  collection is iterated over
  and the provided template function is applied to each value in the collection.
* `Range` Directive - `Range` enables iterating over a range of integer values optionally by a given step.
* `Join` Directive - `Join` enabled joining a collection of templates together with a given separator template.

There are typically two types of logic directive, those that take a simple boolean condition (e.g. x == 1) and
those that take a `Predicate`s which are used into lambda form (e.g. x -> x == 1). The former is more concise but the
latter is more flexible.  
Additionally, the directives that accept a `Predicate` also require a `Function<T, UndeadTemplate>` to be provided. This
means the function that
returns the template has access to the value being tested which is very useful for adding to a template or
using `Function` defined elsewhere.

Examples

```java
// If Directive
HTML."""
  \{ If(zombieCount > 0, HTML."<span>🙀Uh oh, there are zombies!</span>") }
  """;

// If/Else Directive
    HTML."""
  <div>\{ 
    If(brainsEaten < 10, 
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

### Using Undead Templates for "DeadViews" (i.e. traditional) HTML rendering

It is fine to use Undead Templates to render HTML outside of LiveViews so feel free to do so. You can use
the `Undead.HTML` processor
the same way and call `toString()` on it to get the rendered HTML.

## Javascript Commands

Undead provides a number of Javascript commands that can be used to show/hide elements, add/remove classes, dispatch
events, etc. These commands are
called JS Commands and can be part of the Undead Template you define in a `View`s `render` function. JS Commands are
loaded on the client-side
when the template is rendered and executed based on user interactions. JS Commands are a powerful way to manipulate the
DOM without having to write javascript.  
Below is a list of the commands:

* JS.addClass - Adds a class to an element
* JS.dispatch - Dispatches an event on an element
* JS.exec - Executes JS commands on an element's attribute
* JS.focus - Focuses an element
* JS.focusFirst - Focuses the first element in a selector
* JS.hide - Hides an element
* JS.navigate - Sends a navigation event to the server
* JS.patch - Sends a patch event to the server
* JS.popFocus - Pops the focus stack
* JS.push - Pushes an event to the server
* JS.pushFocus - Pushes the focus stack
* JS.removeAttr - Removes an attribute from an element
* JS.removeClass - Removes a class from an element
* JS.setAttr - Sets an attribute on an element
* JS.show - Shows an element
* JS.toggle - Toggles visibility of an element
* JS.transition - Applies css transitions to an element

See the JS Commands javadocs for more details on each command and its options.

## `Undead` Javascript

Like I said, you don't have to write javascript...and if javascript is too ghastly for you to write, you can continue
to use the `undead.js` as is (see the resources directory) and just make sure it is loaded on your page.  
However, if you want to wade into the gory details of the javascript, check out the `/js` directory. Inside is a simple
node project that uses [esbuild](https://esbuild.github.io/) to build the `undead.js` file from typescript
(i.e. `js/undead.ts`). Assuming you have npm installed, to run it:

```shell
cd js
npm install
npm run build
```

## Why did you write Undead?

LiveViews (a.k.a UndeadViews 🧟) are an enchanting, new paradigm for building bewitching front-end experiences and I
wanted to see if how hard it would be to implement them on the JVM. I poked around the existing templating libraries
and didn't find anything that was ideal but when I learned about String Templates I thought they might be a good fit
and waited for them to be released. Once they were released, it was just a matter of dusting off the ol' Java skills
(still very rusty - thank you very much).

It also helps that I've written (or co-written) LiveView libraries for [Javascript](https://liveviewjs)
and [Go](https://github.com/canopyclimate/golive) so know the
protocol fairly well at this point.

## TODO

Most of these are advanced features or internal details that you don't need to know about to use Undead.
~~This is very early / PoC stage / "nothing works".~~ The basics work:

- [x] Templating - `Undead.HTML`, `If`, `Switch`, `Map`, `Range`, `Join` Directives
- [x] Message JSON parsing and generation
- [x] Basic Protocol `phx-join`, `heartbeat`, page events (`click`, `keyup`, `blur`, etc) and `form` events
- [x] Internal Events (i.e. InfoEvents)
- [x] Example Project - Javalin + Undead with examples: `UndeadCounter`, `UndeadUserForm`, `UndeadSalesDashnoard`
- [ ] Parts Diffing
- [ ] Push Events
- [ ] Live Patch / Navigation
- [ ] File Uploads
- [ ] UndeadComponents (i.e. LiveComponents)
- [x] Building Client Javascript
- [x] JS Commands
- [ ] Pub/Sub

## Feedback Welcome

I haven't written Java professionally in about 12 years so would love any feedback and/or PRs!

## Elixir / Erlang folks

Please don't [Greenspun's tenth rule](https://en.wikipedia.org/wiki/Greenspun%27s_tenth_rule) me about "the BEAM" 🙀😀.
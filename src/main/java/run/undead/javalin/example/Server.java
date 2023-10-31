package run.undead.javalin.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import run.undead.config.Config;
import run.undead.javalin.example.view.UndeadCounter;
import run.undead.javalin.example.view.UndeadSalesDashboard;
import run.undead.javalin.example.view.UndeadUserForm;
import run.undead.javalin.example.view.tags.ExCard;
import run.undead.template.Directive;
import run.undead.template.Undead;

import java.util.List;

/**
 * Server is a simple Javalin server that uses Undead to render rich, dynamic views.
 */
public class Server {
  public static void main(String[] args) {

    var undeadConf = new Config();
    undeadConf.debug = msg -> System.out.println("Undead: " + msg);
    var app = new UndeadJavalin(Javalin.create(config ->
        // register static file locations for the Undead client javascript
        config.staticFiles.add(staticFileConfig -> {
          staticFileConfig.directory = "/public/js";
          staticFileConfig.location = Location.CLASSPATH;
          staticFileConfig.hostedPath = "/js";
        })
    ), undeadConf)
        // use the UndeadJavalin instance to register Undead Views to routes
        .undead("/count", new UndeadCounter())
        .undead("/count/{start}", new UndeadCounter())
        .undead("/dashboard", new UndeadSalesDashboard())
        .undead("/user/new", new UndeadUserForm())
        .javalin() // get the underlying Javalin instance from UndeadJavalin
        .get("/", ctx -> {

          var examples = List.of(
              new ExCard("Counter", "A simple counter that can be incremented and decremented.", "/count"),
              new ExCard("Sales Dashboard", "A sales dashboard that shows sales data in a chart.", "/dashboard"),
              new ExCard("User Form", "A user form that shows how easy it is to use forms.", "/user/new")
          );
          var html = Undead.HTML."""
          <!DOCTYPE html>
          <html lang="en">
            <head>
              <meta charset="utf-8" />
              <meta http-equiv="X-UA-Compatible" content="IE=edge" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0" />
              <title>Undead Examples</title>
              <!-- DaisyUI + Tailwind CSS: we recommend replacing this with your own CSS/Components -->
              <link href="https://cdn.jsdelivr.net/npm/daisyui@3.9.2/dist/full.css" rel="stylesheet" type="text/css" />
              <script src="https://cdn.tailwindcss.com"></script>
            </head>
            <body>
              <div class="navbar bg-base-100 mb-4 border-b">
                <a class="btn btn-ghost normal-case text-xl">🧟Undead</a>
              </div>
                <div class="flex gap-x-4 justify-center items-center">
                  \{ Directive.Map(examples, ex -> ex.render()) }
                </div>
            </body>
          </html>
          """;
          ctx.result(html.toString());
          ctx.contentType("text/html");
        })
        .start(1313);
  }
}


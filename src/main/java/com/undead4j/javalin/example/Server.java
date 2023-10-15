package com.undead4j.javalin.example;

import com.undead4j.Config;
import com.undead4j.javalin.UndeadHandler;
import com.undead4j.socket.WsHandler;
import com.undead4j.template.LiveTemplate;
import com.undead4j.template.PageTemplate;
import com.undead4j.template.PageTitleConfig;
import io.javalin.Javalin;
import io.javalin.config.RoutingConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.routing.PathParser;

import static com.undead4j.template.Live.HTML;
import static com.undead4j.template.Live.NO_ESC;

public class Server {
  public static void main(String[] args) {
    var liveConf = new Config();
    liveConf.pageTemplate = new PageTemplate() {
      @Override
      public LiveTemplate render(PageTitleConfig pageTitleConfig, String csrfToken, LiveTemplate content) {
        return HTML. """
            <!DOCTYPE html>
            <html lang="en" class="h-full bg-white">
              <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <meta name="csrf-token" content="\{ csrfToken }" />
                \{ this.liveTitle(pageTitleConfig) }
                <script defer type="text/javascript" src="/js/index.js"></script>
                <!-- Tailwind CSS: we recommend replacing this with your own CSS -->
                <link href="https://cdn.jsdelivr.net/npm/daisyui@3.9.2/dist/full.css" rel="stylesheet" type="text/css" />
                <script src="https://cdn.tailwindcss.com"></script>
              </head>
              <body>
                <div class="navbar bg-base-100">
                  <a class="btn btn-ghost normal-case text-xl">🧟Undead4j</a>
                </div>
                <!-- Embedded LiveView -->
                \{ NO_ESC(content) }
              </body>
            </html>
            """ ;
      }
    };

    var app = new UndeadJavalin(Javalin.create(config -> {
          config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.directory = "/public/js";
            staticFileConfig.location = Location.CLASSPATH;
            staticFileConfig.hostedPath = "/js";
          });
        }), liveConf)
        .undead("/count", new UndeadHandler(liveConf, new CounterLiveView()))
        .get("/count/{start}", ctx -> {

          var parser = new PathParser("/count/{start}", new RoutingConfig());
          System.out.println("Matches:" + parser.matches(ctx.path()) + " matches:" + parser.matches("/count"));
          ctx.result("You are here because " + ctx.path() + " matches " + ctx.matchedPath() + " paramMap " + ctx.pathParamMap());
        })
        .get("/", ctx -> {
          ctx.result("Hello");
          ctx.contentType("text/html");
        })
        .start(7070);

    app.ws("/live/websocket", ws -> { new WsHandler(liveConf, ws);});
  }
}


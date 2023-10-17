package com.undead4j.javalin.example;

import com.undead4j.Config;
import com.undead4j.javalin.UndeadHandler;
import com.undead4j.javalin.example.view.UndeadCounter;
import com.undead4j.javalin.example.view.UndeadSalesDashboard;
import com.undead4j.javalin.example.view.UndeadUserForm;
import com.undead4j.socket.WsHandler;
import com.undead4j.template.LiveTemplate;
import com.undead4j.template.PageTemplate;
import com.undead4j.template.PageTitleConfig;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

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
            <html lang="en">
              <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <meta name="csrf-token" content="\{ csrfToken }" />
                \{ this.liveTitle(pageTitleConfig) }
                <script defer type="text/javascript" src="/js/index.js"></script>
                <!-- DaisyUI + Tailwind CSS: we recommend replacing this with your own CSS/Components -->
                <link href="https://cdn.jsdelivr.net/npm/daisyui@3.9.2/dist/full.css" rel="stylesheet" type="text/css" />
                <script src="https://cdn.tailwindcss.com"></script>
              </head>
              <body>
                <div class="navbar bg-base-100 mb-4">
                  <a class="btn btn-ghost normal-case text-xl">🧟Undead4j</a>
                </div>
                <!-- Embedded LiveView -->
                \{ NO_ESC(content) }
              </body>
            </html>
            """ ;
      }
    };

    var app = new UndeadJavalin(Javalin.create(config ->
          config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.directory = "/public/js";
            staticFileConfig.location = Location.CLASSPATH;
            staticFileConfig.hostedPath = "/js";
          })
        ), liveConf)
        // use the UndeadJavalin instance to register Undead4j routes
        .undead("/count", new UndeadHandler(liveConf, new UndeadCounter()))
        .undead("/count/{start}", new UndeadHandler(liveConf, new UndeadCounter()))
        .undead("/dashboard", new UndeadHandler(liveConf, new UndeadSalesDashboard()))
        .undead("/user/new", new UndeadHandler(liveConf, new UndeadUserForm()))
        .javalin() // get the underlying Javalin instance from UndeadJavalin
        .get("/", ctx -> {
          ctx.result("Hello");
          ctx.contentType("text/html");
        })
        .start(7070);

    app.ws("/live/websocket", ws -> new WsHandler(liveConf, ws));
  }
}


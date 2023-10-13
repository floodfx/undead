package com.undead4j.javalin.example;

import com.undead4j.Config;
import com.undead4j.handle.HttpHandler;
import com.undead4j.handle.http.JavalinRequestAdaptor;
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
            <html lang="en" class="h-full bg-white">
              <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <meta name="csrf-token" content="\{ csrfToken }" />
                \{this.liveTitle(pageTitleConfig)}
                <script defer type="text/javascript" src="/js/index.js"></script>
                <!-- Tailwind CSS: we recommend replacing this with your own CSS -->
                <script src="https://cdn.tailwindcss.com"></script>
              </head>
              <body>
                <!-- Embedded LiveView -->
                \{NO_ESC(content)}
              </body>
            </html>
            """;
      }
    };

    var app = Javalin.create(config -> {
          config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.directory = "/public/js";
            staticFileConfig.location = Location.CLASSPATH;
            staticFileConfig.hostedPath = "/js";
          });
        })
        .get("/count", ctx -> {
            var res = new HttpHandler().handle(
                new CounterLiveView(new Counter()),
                liveConf.pageTemplate,
                new JavalinRequestAdaptor(ctx),
                new PageTitleConfig().prefix("Prefix").title("Title").suffix("Suffix"),
                liveConf.wrapperTemplate
            );
            ctx.result(res);
            ctx.contentType("text/html");
        })
        .get("/", ctx -> {
          ctx.result("Hello");
          ctx.contentType("text/html");
        })
        .start(7070);

    app.ws("/live/websocket", ws -> {
      ws.onConnect(ctx -> {
        System.out.println("Connected:" + ctx.getSessionId());
      });
      ws.onClose(ctx -> {
        System.out.println("Closed:" + ctx.getSessionId());
      });
      ws.onMessage(ctx -> {
        System.out.println("Msg:" + ctx.getSessionId() + ctx.message());
      });
    });
  }
}


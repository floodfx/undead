package com.undead4j.javalin.example;

import com.undead4j.template.LiveTemplate;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.Map;

import static com.undead4j.template.Live.HTML;

public class Server {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
                    config.staticFiles.add(staticFileConfig -> {
                        staticFileConfig.directory = "/public/js";
                        staticFileConfig.location = Location.CLASSPATH;
                        staticFileConfig.hostedPath = "/js";
                    });
                })
                .get("/", ctx -> {
                    var html = Server.getMainTemplate("id","token", "title");
                    ctx.result(html.toString());
                    ctx.contentType("text/html");
                })
                .start(7070);

        app.ws("/live/websocket", ws -> {
            ws.onConnect(ctx -> {
               System.out.println("Connected:"+ctx.getSessionId());
            });
            ws.onClose(ctx -> {
                System.out.println("Closed:"+ctx.getSessionId());
            });
            ws.onMessage(ctx -> {
                System.out.println("Msg:"+ctx.message());
            });
        });
    }

    static LiveTemplate getMainTemplate(String liveViewID, String csrfToken, String title) {
        return HTML."""
            <!DOCTYPE html>
                <html lang="en" class="h-full bg-white">
                  <head>
                    <meta charset="utf-8" />
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <meta name="csrf-token" content="\{ csrfToken }" />
                    <title>\{title}</title>
                    <script defer type="text/javascript" src="/js/index.js"></script>
                    <!-- Tailwind CSS: we recommend replacing this with your own CSS -->
                    <script src="https://cdn.tailwindcss.com"></script>
                  </head>
                  <body>
                    <!-- Embedded LiveView -->
                    <div
                      data-phx-main="true"
                      data-phx-session="${serializedSession}"
                      data-phx-static="${serializedStatics}"
                      id="phx-\{liveViewID}">
                      ${safe(liveViewContent)}
                    </div>
                    ${safe(liveViewContent)}
                  </body>
                </html>
            """ ;
    }
}


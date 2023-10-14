package com.undead4j.javalin.example;

import com.undead4j.Config;
import com.undead4j.handle.HttpHandler;
import com.undead4j.handle.http.JavalinRequestAdaptor;
import com.undead4j.protocol.MsgParser;
import com.undead4j.protocol.Reply;
import com.undead4j.socket.WsSocket;
import com.undead4j.template.LiveTemplate;
import com.undead4j.template.PageTemplate;
import com.undead4j.template.PageTitleConfig;
import com.undead4j.view.Meta;
import io.javalin.Javalin;
import io.javalin.config.RoutingConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.routing.PathParser;
import okhttp3.HttpUrl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
                <script src="https://cdn.tailwindcss.com"></script>
              </head>
              <body>
                <!-- Embedded LiveView -->
                \{ NO_ESC(content) }
              </body>
            </html>
            """ ;
      }
    };

    var app = Javalin.create(config -> {
          config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.directory = "/public/js";
            staticFileConfig.location = Location.CLASSPATH;
            staticFileConfig.hostedPath = "/js";
          });
        })
        .get("/count/{start}", ctx -> {

          var parser = new PathParser("/count/{start}", new RoutingConfig());
          System.out.println("Matches:" + parser.matches(ctx.path()) + " matches:" + parser.matches("/count"));
          ctx.result("You are here because " + ctx.path() + " matches " + ctx.matchedPath() + " paramMap " + ctx.pathParamMap());
        })
        .get("/count", ctx -> {
          liveConf.register(ctx.path(), CounterLiveView.class);
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
        ctx.session.setIdleTimeout(Duration.of(45, ChronoUnit.SECONDS));
      });
      ws.onError(err -> {
        System.out.println("error:"+err+" "+err.error());
      });
      ws.onClose(ctx -> {
        System.out.println("Closed:" + ctx.getSessionId());
      });
      ws.onMessage(ctx -> {
        var msg = new MsgParser().parseJSON(ctx.message());
        System.out.println("Msg:" + msg + " sess:" + ctx.getSessionId() + " rawMsg:" + ctx.message());
        switch (msg.event()) {
          case "phx_join":
            // check if topic starts with "lv:" or "lvu:"
            // "lv:" is a liveview join
            // "lvu:" is a liveview upload join
            var prefix = msg.topic().split(":");
            switch (prefix[0]) {
              case "lv":
                // first we need to read the msg to see what route we're on
                // on join the message payload should include a "url" key or
                // a "redirect" key
                var urlStr = "";
                if (msg.payload().containsKey("url")) {
                  urlStr = (String) msg.payload().get("url");
                } else if (msg.payload().containsKey("redirect")) {
                  urlStr = (String) msg.payload().get("redirect");
                } else {
                  throw new RuntimeException("no url or redirect key found in payload");
                }
                // parse url
                var url = HttpUrl.parse(urlStr);
                // look up View by url path
                var view = liveConf.matchPath(url.encodedPath());
                if (view == null) {
                  throw new RuntimeException("unable to find view for path:" + url.encodedPath() + " url:" + url);
                }

                // get data from params
                var rawParams = msg.payload().get("params");
                if (rawParams == null) {
                  throw new RuntimeException("params not present in payload");
                }
                var socket = new WsSocket<>(msg.topic(), urlStr);
                // TODO?
//              socket.joinRef = msg.joinRef();
//              socket.msgRef = msg.msgRef();
//              socket.csrfToken = ((Map)msg.payload().get("params")).get("_csrf_token");


                // Join is the initalize event and the only time we call Mount on the view.
                // Only call Mount if the view implements Mounter
                view.mount();

                // Also call HandleParams during join to give the LiveView a chance
                // to update its state based on the URL params
                view.handleParams();

                var content = view.render(socket.context(), new Meta());

                var reply = Reply.NewRendered(msg, content);
                System.out.println("replying with:"+reply);
                ctx.send(reply);
                // TODO case "lvu":
                break;
              default: // unknown phx_join topic
                throw new RuntimeException("unknown phx_join topic");
            }
            break;
          case "heartbeat":
            ctx.send(Reply.NewHeartbeat(msg));
            break;
          default:
            throw new RuntimeException("unhandled event:"+msg.event());
        }
      });
    });
  }
}


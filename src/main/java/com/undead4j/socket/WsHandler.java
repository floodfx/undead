package com.undead4j.socket;

import com.undead4j.Config;
import com.undead4j.event.SimpleUndeadEvent;
import com.undead4j.javalin.JavalinWsAdaptor;
import com.undead4j.protocol.MsgParser;
import com.undead4j.protocol.Reply;
import com.undead4j.template.UndeadTemplate;
import com.undead4j.url.Values;
import com.undead4j.view.Meta;
import io.javalin.websocket.WsConfig;
import okhttp3.HttpUrl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WsHandler {

  private final Map<String, WsSocket> socketRegistry = new ConcurrentHashMap<>();

  public WsHandler(Config liveConf, WsConfig ws) {
    ws.onConnect(ctx -> {
      // client sends heartbeat every 30 seconds which is default idleTimeout
      // so we need to extend default idle timeout to longer
      ctx.session.setIdleTimeout(Duration.of(45, ChronoUnit.SECONDS));
    });
    ws.onError(err -> {
      // give socket a chance to handle error
      getOrCreateSocket(err.getSessionId()).handleError(err);
    });
    ws.onClose(ctx -> {
      // give socket a chance to handle close / cleanup
      getOrCreateSocket(ctx.getSessionId()).handleClose();
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
              var view = liveConf.routeMatcher.matches(url.encodedPath());
              if (view == null) {
                throw new RuntimeException("unable to find view for path:" + url.encodedPath() + " url:" + url);
              }

              // get data from params
              var params = (Map) msg.payload().get("params");
              if (params == null) {
                throw new RuntimeException("params not present in payload");
              }
              var session = (String) msg.payload().get("session");
              // TODO decode session?

              // TODO pull path params from url
              var pathParams = liveConf.routeMatcher.pathParams(url.encodedPath(), urlStr);
              // merge path params into params
              params.putAll(pathParams);

              // configure socket
              var socket = getOrCreateSocket(ctx.getSessionId());
              socket.id = msg.topic();
              socket.url = urlStr;
              socket.joinRef = msg.joinRef();
              socket.msgRef = msg.msgRef();
              socket.csrfToken = (String) params.get("_csrf_token");
              socket.view = view;
              socket.connection = new JavalinWsAdaptor(ctx);
              // Join is the initalize event and the only time we call Mount on the view.
              // TODO get session data and params
              view.mount(socket, Map.of(), params);

              // Also call HandleParams during join to give the LiveView a chance
              // to update its state based on the URL params
              view.handleParams(socket);

              var content = view.render(new Meta());
              var parts = content.toParts();

              ctx.send(Reply.rendered(msg, parts));
              // TODO case "lvu":
              break;
            default: // unknown phx_join topic
              throw new RuntimeException("unknown phx_join topic");
          }
          break;
        case "heartbeat":
          ctx.send(Reply.heartbeat(msg));
          break;
        case "event":
          // determine type of event and further details
          var payloadEventType = (String) msg.payload().get("type");
          var payloadEvent = (String) msg.payload().get("event");
          // TODO handle components
          // var cid = (String)msg.payload().get("cid");
          UndeadTemplate tmpl;
          switch (payloadEventType) {
            case "click":
            case "keyup":
            case "keydown":
            case "blur":
            case "focus":
            case "hook":
              // get value from payload with should be a map
              var payloadValues = Values.from((Map<String, String>) msg.payload().get("value"));
              // convert the value map to a url.Values

              // check if the click is a lv:clear-flash event
              // which does not invoke HandleEvent but should
              // set the flash value to "" and send a responseDiff
              if (payloadEventType.equals("lv:clear-flash")) {
                var flashKey = payloadValues.get("key");
                // TODO implement clear flash
                throw new RuntimeException("clear flash not implemented");
              } else {
                var socket = getSocket(ctx.getSessionId());
                socket.view.handleEvent(socket, new SimpleUndeadEvent(payloadEvent, payloadValues));
              }
              break;
            case "form":
              // for form events the payload value is a string that needs to be parsed into the data
              var values = Values.from((String) msg.payload().get("value"));
              // handle uploads before calling calling handleEvent
              // TODO uploads
              var socket = getSocket(ctx.getSessionId());
              socket.view.handleEvent(socket, new SimpleUndeadEvent(payloadEvent, values));
              break;
            default:
              throw new RuntimeException("unknown event type:" + payloadEventType);
          }
          // check if we have a redirect
          var socket = getSocket(ctx.getSessionId());
          if (socket != null && !socket.redirect().isEmpty()) {
            ctx.send(Reply.redirect(msg, socket.redirect()));
            break;
          }
          // otherwise rerender
          var content = socket.view().render(new Meta());
          var parts = content.toParts();
          ctx.send(Reply.replyDiff(msg, parts));
          break;
        default:
          throw new RuntimeException("unhandled event:" + msg.event());
      }

    });
  }

  private WsSocket getOrCreateSocket(String id) {
    var socket = socketRegistry.get(id);
    if (socket == null) {
      socket = new WsSocket();
      socketRegistry.put(id, socket);
    }
    return socket;
  }

  private WsSocket getSocket(String id) {
    return socketRegistry.get(id);
  }
}

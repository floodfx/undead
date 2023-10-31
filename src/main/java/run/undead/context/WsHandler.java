package run.undead.context;

import run.undead.config.Config;
import run.undead.event.SimpleUndeadEvent;
import run.undead.protocol.MsgParser;
import run.undead.protocol.Reply;
import run.undead.template.UndeadTemplate;
import run.undead.url.Values;
import run.undead.view.Meta;
import okhttp3.HttpUrl;
import run.undead.view.View;

import java.util.Map;

/**
 * WsHandler handles the websocket request lifecycle for a {@link View}
 */
public class WsHandler {
  private final Config undeadConfig;
  private WsSender wsSender;
  private WsContext context;

  public WsHandler(Config undeadConfig) {
    this.undeadConfig = undeadConfig;
  }

  public void setWsSender(WsSender wsSender) {
    this.wsSender = wsSender;
  }

  public void handleMessage(String message) throws Exception {
      var msg = new MsgParser().parseJSON(message);
      switch (msg.event()) {
        case "heartbeat":
          wsSender.send(Reply.heartbeat(msg));
          break;
        case "phx_join":
          // check if topic starts with "lv:" or "lvu:"
          // "lv:" is a live view join
          // "lvu:" is a live view upload join
          var prefix = msg.topic().split(":");
          switch (prefix[0]) {
            case "lv":
              // for "lv" joins the payload should include a "url" or "redirect" key
              // from which we can to look up the View
              var urlStr = "";
              if (msg.payload().containsKey("url")) {
                urlStr = (String) msg.payload().get("url");
              } else if (msg.payload().containsKey("redirect")) {
                urlStr = (String) msg.payload().get("redirect");
              } else {
                throw new RuntimeException("no url or redirect key found in payload");
              }
              // extract the path from the URL and match it using the route matcher
              var url = HttpUrl.parse(urlStr);
              var path = url.encodedPath();
              var view = undeadConfig.routeMatcher.matches(path);
              if (view == null) {
                throw new RuntimeException("unable to find view for path:" + path + " url:" + urlStr);
              }

              // get data from params
              var params = (Map) msg.payload().get("params");
              if (params == null) {
                throw new RuntimeException("params not present in payload");
              }

              // TODO decode session
              var session = (String) msg.payload().get("session");
              // TODO pull path params from url
              var pathParams = undeadConfig.routeMatcher.pathParams(path);
              // merge path params into params
              params.putAll(pathParams);

              // setup WS context
              context = new WsContext(msg.topic(), urlStr, view);
              context.joinRef = msg.joinRef();
              context.msgRef = msg.msgRef();
              context.csrfToken = (String) params.get("_csrf_token");
              context.view = view;
              context.sender = wsSender;
              // TODO get session data and params

              // lv: join messages get a mount => handleParams => render
              view.mount(context, Map.of(), params);
              view.handleParams(context, url.uri(), params);
              var content = view.render(new Meta());

              // instead of serializing as HTML string, we send back the parts data structure
              var parts = content.toParts();
              wsSender.send(Reply.rendered(msg, parts));
              break;
            // TODO case "lvu" i.e. uploads
            default: // unknown phx_join topic
              throw new RuntimeException("unknown phx_join topic");
          }
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
                // handle the event
                this.context.view.handleEvent(context, new SimpleUndeadEvent(payloadEvent, payloadValues));
              }
              break;
            case "form":
              // for form events the payload value is a string that needs to be parsed into the data
              var values = Values.from((String) msg.payload().get("value"));
              // handle uploads before calling calling handleEvent
              // TODO uploads
              this.context.view.handleEvent(context, new SimpleUndeadEvent(payloadEvent, values));
              break;
            default:
              throw new RuntimeException("unknown event type:" + payloadEventType);
          }
          // check if we have a redirect
          if (context != null && !context.redirect.isEmpty()) {
            wsSender.send(Reply.redirect(msg, context.redirect));
            break;
          }
          // otherwise rerender
          var content = context.view.render(new Meta());
          var parts = content.toParts();
          wsSender.send(Reply.replyDiff(msg, parts));
          break;
        default:
          throw new RuntimeException("unhandled event:" + msg.event());
      }
    }

  public void handleError(Object error) {
    if (this.context != null) {
      this.context.handleError(error);
    }
  }

  public void handleClose() {
    if (this.context != null) {
      this.context.handleClose();
    }
  }



}

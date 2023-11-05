package run.undead.context;

import run.undead.event.SimpleUndeadInfo;
import run.undead.event.UndeadEvent;
import run.undead.event.UndeadInfo;
import run.undead.protocol.Reply;
import run.undead.pubsub.PubSub;
import run.undead.template.UndeadTemplate;
import run.undead.view.Meta;
import run.undead.view.View;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WsContext is an implementation of the {@link Context} for the WebSocket
 * lifecycle.
 */
public class WsContext implements Context {
  protected String id;
  protected String url;
  protected View view;
  protected String joinRef;
  protected String msgRef;
  protected String csrfToken;
  protected String redirect;
  protected WsSender sender;
  protected Map<String, String> subs; // topic to subId
  protected PubSub pubsub;
  protected UndeadTemplate lastTmpl;
  protected List<UndeadEvent> events;
  protected String title;

  public WsContext(String id, String url, View view) {
    this.id = id;
    this.url = url;
    this.view = view;
    this.subs = new ConcurrentHashMap<>();
  }

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String url() {
    return this.url;
  }

  @Override
  public Boolean connected() {
    return true;
  }

  @Override
  public void pageTitle(String newTitle) {
    this.title = newTitle;
  }

  @Override
  public void pushEvent(UndeadEvent event) {
    this.events.add(event);
  }

  @Override
  public void sendInfo(UndeadInfo info) {
    this.view.handleInfo(this, info);
    var content = this.view.render(new Meta());
    this.sender.send(Reply.diff(this.id, diffParts(content)));
  }

  @Override
  public void redirect(String url) {
    this.redirect = url;
  }

  @Override
  public void subscribe(String topic) {
    if(this.pubsub == null) {
      throw new RuntimeException("pubsub not set");
    }
    // check if already subscribed
    if(this.subs.containsKey(topic)) {
      return;
    }
    var subId = this.pubsub.subscribe(topic, (t, m) -> {
      this.sendInfo(new SimpleUndeadInfo(t, m));
    });
    // save topic to sub mapping
    this.subs.put(topic, subId);
  }

  @Override
  public void unsubscribe(String topic) {
    if(this.pubsub == null) {
      throw new RuntimeException("pubsub not set");
    }
    // check if we have a subId for this topic
    if(!this.subs.containsKey(topic)) {
      return;
    }
    this.pubsub.unsubscribe(this.subs.get(topic));
  }

  @Override
  public void publish(String topic, String data) {
    if(this.pubsub == null) {
      throw new RuntimeException("pubsub not set");
    }
    this.pubsub.publish(topic, data);
  }

  public void handleClose() {
    if(this.pubsub != null) {
      for(var topic : subs.keySet()) {
        this.unsubscribe(topic);
      }
    }
    if (this.view != null) {
      this.view.shutdown();
    }
  }

  public void handleError(Object err) {
    // TODO send something to client to reload?
    this.handleClose();
  }

  /**
   * diffParts takes the new template and diffs it with the last template (if there was one)
   * and returns the diff "parts" to send to the client.  Additionally, this method will
   * add the title and events to the parts if they are set.
   * @param newTmpl the new template to diff with the last template
   * @return the diff "parts" to send to the client
   */
  public Map<String, Object> diffParts(UndeadTemplate newTmpl) {
    var oldTmpl = this.lastTmpl;
    this.lastTmpl = newTmpl;
    var parts = newTmpl.toParts();
    // if we have an old template diff with new template
    if(oldTmpl != null) {
      parts = UndeadTemplate.diff(oldTmpl.toParts(), newTmpl.toParts());
    }
    // add title if set
    if(this.title != null) {
      parts.put("t", this.title);
      this.title = null;
    }
    // add events if set
    if(this.events != null) {
      parts.put("e", this.events);
      this.events = null;
    }
    return parts;
  }
}

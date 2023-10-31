package com.undead4j.context;

import com.undead4j.event.UndeadEvent;
import com.undead4j.event.UndeadInfo;
import com.undead4j.protocol.Reply;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

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

  public WsContext(String id, String url, View view) {
    this.id = id;
    this.url = url;
    this.view = view;
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
    // TODO add "t"
  }

  @Override
  public void pushEvent(UndeadEvent event) {

  }

  @Override
  public void sendInfo(UndeadInfo info) {
    this.view.handleInfo(this, info);
    var tmpl = this.view.render(new Meta());
    this.sender.send(Reply.diff(this.id, tmpl.toParts()));
  }

  @Override
  public void redirect(String url) {
    this.redirect = url;
  }

  public void handleClose() {
    if (this.view != null) {
      this.view.shutdown();
    }
  }

  public void handleError(Object err) {
    // TODO send something to client to reload?
    if (this.view != null) {
      this.view.shutdown();
    }
  }
}

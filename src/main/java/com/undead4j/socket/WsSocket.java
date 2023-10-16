package com.undead4j.socket;

import com.undead4j.event.UndeadInfo;
import com.undead4j.protocol.Reply;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

public class WsSocket<Context> implements Socket<Context> {
  protected String id;
  protected String url;
  private Context context;
  protected View view;
  protected String joinRef;
  protected String msgRef;
  protected String csrfToken;
  protected String redirect = "";

  protected WsAdaptor connection;

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public String url() {
    return this.url;
  }

  public View view() {
    return this.view;
  }

  @Override
  public Boolean connected() {
    return true;
  }

  @Override
  public Context context() {
    return null;
  }

  @Override
  public void assign(Context ctx) {
    this.context = ctx;
  }

  @Override
  public void tempAssign(Context ctx) {

  }

  @Override
  public void pageTitle(String newTitle) {

  }

  @Override
  public void pushEvent(Object event) {

  }

  @Override
  public void sendInfo(UndeadInfo info) {
    this.view.handleInfo(this, info);
    var tmpl = this.view.render(new Meta());
    this.connection.send(Reply.diff(this.id, tmpl.toParts()));
  }

  public String redirect() {
    return redirect;
  }

  public void handleClose() {
    if(this.view != null) {
      this.view.shutdown();
    }
  }

  public void handleError(Object err) {
    System.out.println("handlingError:"+err);
    // TODO send something to client to reload?
    if(this.view != null) {
      this.view.shutdown();
    }
  }
}

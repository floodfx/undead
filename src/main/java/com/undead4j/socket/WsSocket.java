package com.undead4j.socket;

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

  private WsAdaptor wsAdaptor;

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

  public String redirect() {
    return redirect;
  }

  public void handleClose() {
    System.out.println("handlingClose:");
    if(this.view != null) {
      this.view.shutdown();
    }
  }

  public void handleError(Object err) {
    System.out.println("handlingError:"+err);
    if(this.view != null) {
      this.view.shutdown();
    }
  }
}

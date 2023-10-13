package com.undead4j.socket;

public class HttpSocket<Context> implements Socket<Context> {
  private String id;
  private String url;
  private Context context;
  public HttpSocket(String id, String url) {
    this.id = id;
    this.url = url;
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
    return false;
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
    return null;
  }
}

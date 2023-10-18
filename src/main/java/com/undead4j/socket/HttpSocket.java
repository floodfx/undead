package com.undead4j.socket;

import com.undead4j.event.UndeadInfo;
import com.undead4j.view.View;

public class HttpSocket<Context> implements Socket<Context> {
  private final String id;
  private final String url;
  private Context context;
  private View view;

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
  public View view() {
    return this.view;
  }

  protected void setView(View view) {
    this.view = view;
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

  @Override
  public void sendInfo(UndeadInfo info) {

  }

  public String redirect() {
    return null;
  }
}

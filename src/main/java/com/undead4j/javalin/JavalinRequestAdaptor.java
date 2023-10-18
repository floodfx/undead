package com.undead4j.javalin;

import com.undead4j.handle.http.RequestAdaptor;
import io.javalin.http.Context;

import java.util.Map;

public class JavalinRequestAdaptor implements RequestAdaptor {
  private final Context ctx;
  private String redirectURL;

  public JavalinRequestAdaptor(Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public Map<String, Object> sessionData() {
    return this.ctx.sessionAttributeMap();
  }

  @Override
  public String url() {
    return this.ctx.url();
  }

  @Override
  public String path() {
    return this.ctx.path();
  }

  @Override
  public Map pathParams() {
    return ctx.pathParamMap();
  }

  @Override
  public void willRedirect(String destURL) {
    this.redirectURL = destURL;
  }
}

package com.undead4j.context;

/**
 * HttpContext implements the {@link Context} interface for the HTTP request
 * lifecycle.
 */
public class HttpContext implements Context {
  private final String id;
  private final String url;

  public String redirect;

  public HttpContext(String id, String url) {
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
  public void redirect(String url) {
    this.redirect = url;
  }
}

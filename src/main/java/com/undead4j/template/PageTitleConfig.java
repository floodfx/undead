package com.undead4j.template;

public class PageTitleConfig {
  public String title;
  public String prefix;
  public String suffix;
  public PageTitleConfig title(String title) {
    this.title = title;
    return this;
  }

  public PageTitleConfig prefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public PageTitleConfig suffix(String suffix) {
    this.suffix = suffix;
    return this;
  }


}

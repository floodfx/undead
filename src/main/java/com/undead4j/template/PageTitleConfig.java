package com.undead4j.template;

import org.jetbrains.annotations.NotNull;

public class PageTitleConfig {
  private String title;
  private String prefix;
  private String suffix;

  public PageTitleConfig() {
    this.title = "";
    this.prefix = "";
    this.suffix = "";
  }

  public String title() {
    return this.title;
  }

  public String prefix() {
    return this.prefix;
  }

  public String suffix() {
    return this.suffix;
  }

  public PageTitleConfig withTitle(@NotNull String title) {
    this.title = title;
    return this;
  }

  public PageTitleConfig withPrefix(@NotNull String prefix) {
    this.prefix = prefix;
    return this;
  }

  public PageTitleConfig withSuffix(@NotNull String suffix) {
    this.suffix = suffix;
    return this;
  }


}

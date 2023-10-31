package com.undead4j.template;

import org.jetbrains.annotations.NotNull;

public class PageTitle {
  private String title;
  private String prefix;
  private String suffix;

  public PageTitle() {
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

  public PageTitle withTitle(@NotNull String title) {
    this.title = title;
    return this;
  }

  public PageTitle withPrefix(@NotNull String prefix) {
    this.prefix = prefix;
    return this;
  }

  public PageTitle withSuffix(@NotNull String suffix) {
    this.suffix = suffix;
    return this;
  }


}

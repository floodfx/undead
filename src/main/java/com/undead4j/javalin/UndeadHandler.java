package com.undead4j.javalin;

import com.undead4j.Config;
import com.undead4j.handle.http.JavalinRequestAdaptor;
import com.undead4j.socket.HttpHandler;
import com.undead4j.template.PageTitleConfig;
import com.undead4j.view.View;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class UndeadHandler implements Handler {
  private Config config;
  private View view;

  private PageTitleConfig pageTitleConfig;

  public UndeadHandler(Config config, View view) {
    this.config = config;
    this.view = view;
    this.pageTitleConfig = new PageTitleConfig();
  }

  public View view() {
    return this.view;
  }

  public UndeadHandler withPageTitleConfig(PageTitleConfig config) {
    this.pageTitleConfig = config;
    return this;
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    var res = new HttpHandler().handle(
        this.view.getClass().newInstance(),
        this.config.pageTemplate,
        new JavalinRequestAdaptor(ctx),
        this.pageTitleConfig,
        this.config.wrapperTemplate
    );
    ctx.contentType("text/html");
    ctx.result(res);
  }
}

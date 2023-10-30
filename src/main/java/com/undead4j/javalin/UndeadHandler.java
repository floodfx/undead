package com.undead4j.javalin;

import com.undead4j.Config;
import com.undead4j.socket.HttpHandler;
import com.undead4j.template.PageTitleConfig;
import com.undead4j.view.View;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

/**
 * A Javalin {@link Handler} that handles the HTTP request lifecycle of Undead {@link View}s
 */
public class UndeadHandler implements Handler {
  private final Config config;
  private final View view;
  private PageTitleConfig pageTitleConfig;

  /**
   * Constructs a new {@link UndeadHandler} with the given {@link Config} and {@link View}.
   * @param config the {@link Config} to use
   * @param view the {@link View} to render
   */
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
    var res = HttpHandler.handle(
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

package run.undead.javalin;

import run.undead.config.Config;
import run.undead.context.HttpHandler;
import run.undead.template.PageTitle;
import run.undead.view.View;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

/**
 * A Javalin {@link Handler} that handles the HTTP request lifecycle of Undead {@link View}s
 */
public class UndeadHandler implements Handler {
  private final Config config;
  private final View view;
  private PageTitle pageTitle;

  /**
   * Constructs a new {@link UndeadHandler} with the given {@link Config} and {@link View}.
   * @param config the {@link Config} to use
   * @param view the {@link View} to render
   */
  public UndeadHandler(Config config, View view) {
    this.config = config;
    this.view = view;
    this.pageTitle = new PageTitle();
  }

  public View view() {
    return this.view;
  }

  public UndeadHandler withPageTitleConfig(PageTitle config) {
    this.pageTitle = config;
    return this;
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    var res = HttpHandler.handle(
        this.view.getClass().newInstance(),
        this.config.mainLayout,
        new JavalinRequestAdaptor(ctx),
        this.pageTitle,
        this.config.wrapperTemplate
    );
    ctx.contentType("text/html");
    ctx.result(res);
  }
}

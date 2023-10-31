package run.undead.javalin.example;

import run.undead.config.Config;
import run.undead.javalin.JavalinRouteMatcher;
import run.undead.javalin.JavalinWsAdaptor;
import run.undead.javalin.UndeadHandler;
import run.undead.view.View;
import io.javalin.Javalin;
import io.javalin.config.RoutingConfig;
import io.javalin.http.HandlerType;

/**
 * UndeadJavalin is a wrapper around Javalin that provides a simple API for registering
 * {@link View} to server routes.  This automatically registers the
 * {@link UndeadHandler} with the Javalin server and configures
 * WebSocket support for Undead {@link View}s as well.
 */
public class UndeadJavalin {
  private final Config config;
  private final Javalin app;
  private RoutingConfig routingConfig;

  public UndeadJavalin(Javalin app, Config config) {
    this.app = app;
    this.config = config;
    if(this.config.routeMatcher == null) {
      // register route matcher with the config
      this.config.routeMatcher = new JavalinRouteMatcher(this.app.cfg.routing);
    }
    // register websocket handler with javalin
    app.ws("/live/websocket", ws -> new JavalinWsAdaptor(config, ws));
  }

  /**
   * Registers an Undead {@link View} with the Javalin server for the
   * given path.  All {@link UndeadHandler}s are registered as GET handlers.
   * @see View
   * @param path the path that routes to the {@link View}
   * @param view the {@link View} to register
   * @return the UndeadJavalin instance for chaining
   */
  public UndeadJavalin undead(String path, View view) {
    this.config.routeMatcher.addRoute(path, view);
    app.addHandler(HandlerType.GET, path, new UndeadHandler(this.config, view));
    return this;
  }

  /**
   * Get the underlying Javalin instance.
   * @return the underlying Javalin instance
   */
  public Javalin javalin() {
    return this.app;
  }

}

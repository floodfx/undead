package com.undead4j.javalin.example;

import com.undead4j.config.Config;
import com.undead4j.javalin.JavalinRouteMatcher;
import com.undead4j.javalin.JavalinWsAdaptor;
import com.undead4j.javalin.UndeadHandler;
import com.undead4j.view.View;
import io.javalin.Javalin;
import io.javalin.config.RoutingConfig;
import io.javalin.http.HandlerType;

/**
 * UndeadJavalin is a wrapper around Javalin that provides a simple API for registering
 * {@link com.undead4j.view.View} to server routes.  This automatically registers the
 * {@link com.undead4j.javalin.UndeadHandler} with the Javalin server and configures
 * WebSocket support for Undead {@link com.undead4j.view.View}s as well.
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
   * Registers an Undead {@link com.undead4j.view.View} with the Javalin server for the
   * given path.  All {@link UndeadHandler}s are registered as GET handlers.
   * @see com.undead4j.view.View
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

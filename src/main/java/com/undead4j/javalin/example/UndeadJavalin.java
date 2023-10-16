package com.undead4j.javalin.example;

import com.undead4j.Config;
import com.undead4j.javalin.JavalinRouteMatcher;
import com.undead4j.javalin.UndeadHandler;
import io.javalin.Javalin;
import io.javalin.config.RoutingConfig;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

public class UndeadJavalin {
  private Config config;
  private Javalin app;
  private RoutingConfig routingConfig;

  public UndeadJavalin(Javalin app, Config config) {
    this.app = app;
    this.config = config;
    this.config.routeMatcher = new JavalinRouteMatcher(this.app.cfg.routing);
  }

  public UndeadJavalin undead(@NotNull String path, @NotNull UndeadHandler handler) {
    this.config.routeMatcher.addRoute(path, handler.view());
    app.addHandler(HandlerType.GET, path, handler);
    return this;
  }
  public UndeadJavalin live(@NotNull String path, @NotNull UndeadHandler handler) {
    return this.undead(path, handler);
  }

  public Javalin javalin() {
    return this.app;
  }

}

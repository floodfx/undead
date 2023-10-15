package com.undead4j.javalin;

import com.undead4j.view.RouteMatcher;
import com.undead4j.view.View;
import io.javalin.config.RoutingConfig;
import io.javalin.routing.PathParser;

import java.util.LinkedHashMap;
import java.util.Map;

public class JavalinRouteMatcher implements RouteMatcher {
  private RoutingConfig routingConfig;
  private Map<String,Class> routeRegistry;
  public JavalinRouteMatcher(RoutingConfig routingConfig) {
    this.routingConfig = routingConfig;
    this.routeRegistry = new LinkedHashMap();
  }

  @Override
  public View matches(String path) {
    // iterate through map keys trying to find a match using PathParser
    for(var entry : this.routeRegistry.entrySet()) {
      var parser = new PathParser(entry.getKey(), this.routingConfig);
      if(parser.matches(path)) {
        try {
          return (View)entry.getValue().newInstance();
        } catch(Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return null;
  }

  @Override
  public void addRoute(String path, View view) {
    this.routeRegistry.put(path, view.getClass());
  }
}

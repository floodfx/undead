package com.undead4j.javalin;

import com.undead4j.view.RouteMatcher;
import com.undead4j.view.View;
import io.javalin.config.RoutingConfig;
import io.javalin.routing.PathParser;

import java.util.LinkedHashMap;
import java.util.Map;

public class JavalinRouteMatcher implements RouteMatcher {
  private final RoutingConfig routingConfig;
  private final Map<String, Class> routeRegistry;

  public JavalinRouteMatcher(RoutingConfig routingConfig) {
    this.routingConfig = routingConfig;
    this.routeRegistry = new LinkedHashMap();
  }

  public Map pathParams(String path, String url) {
    // first find the matching path regex in the routeRegistry
    var pathRegex = "";
    for (var entry : this.routeRegistry.entrySet()) {
      var parser = new PathParser(entry.getKey(), this.routingConfig);
      if (parser.matches(path)) {
        pathRegex = entry.getKey();
        break;
      }
    }
    if (pathRegex.equals("")) {
      throw new RuntimeException("unable to find matching path regex for path:" + path);
    }
    // now extract the path params
    var parser = new PathParser(pathRegex, this.routingConfig);
    return parser.extractPathParams(path);
  }

  @Override
  public View matches(String path) {
    // iterate through map keys trying to find a match using PathParser
    for (var entry : this.routeRegistry.entrySet()) {
      var parser = new PathParser(entry.getKey(), this.routingConfig);
      if (parser.matches(path)) {
        try {
          return (View) entry.getValue().newInstance();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return null;
  }

  @Override
  public void addRoute(String pathRegex, View view) {
    this.routeRegistry.put(pathRegex, view.getClass());
  }
}

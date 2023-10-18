package com.undead4j.view;

import java.util.Map;

public interface RouteMatcher {
  View matches(String path);

  void addRoute(String path, View view);

  Map pathParams(String pathConfig, String url);
}

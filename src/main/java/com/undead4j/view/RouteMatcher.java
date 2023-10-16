package com.undead4j.view;

import java.util.Map;

public interface RouteMatcher {
  public View matches(String path);

  public void addRoute(String path, View view);

  public Map pathParams(String pathConfig, String url);
}

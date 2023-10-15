package com.undead4j.view;

public interface RouteMatcher {
  public View matches(String path);

  public void addRoute(String path, View view);
}

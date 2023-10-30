package com.undead4j.view;

import java.util.Map;

/**
 * RouteMatcher is an interface for matching an HTTP request path to a view and extracting path parameters
 * from a path.  When implementing a RouteMatcher for a particular server implementation you should use the
 * same regex matching logic that the server uses for its routes and path parameters.
 */
public interface RouteMatcher {

  /**
   * addRoute adds a route mapping the pathRegex to the {@link View}.
   *
   * @param pathRegex the pathRegex to match against
   * @param view the {@link View} to return if the path matches
   */
  public void addRoute(String pathRegex, View view);

  /**
   * matches returns a view if the path matches a route, otherwise null.  Implementations should
   * use the same regex matching logic that the server uses for its routes.
   *
   * @param path the path to match against the pathRegex previously added with addRoute
   * @return the {@link View} if the path matches a route, otherwise null
   */
  public View matches(String path);

  /**
   * pathParams returns a map of path parameters extracted from the path using the previously
   * added pathRegex.  Implementations should use the same regex matching logic that the server
   * uses to extract path parameters.
   * @param path the path to extract path parameters from
   * @return a map of path parameters
   */
  public Map pathParams(String path);

}

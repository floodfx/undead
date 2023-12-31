package run.undead.handle.http;

import java.util.Map;

/**
 * RequestAdaptor is an interface for adapting the HTTP request from a server
 * framework to the Undead render lifecycle.  This normalizes the HTTP request
 * so that the Undead render lifecycle can be used with any server framework that
 * can adapt its request to this interface.
 */
public interface RequestAdaptor {

  /**
   * The session data from the HTTP request
   * @return a Map of session data
   */
  Map sessionData();

  /**
   * The URL of the HTTP request
   * @return the URL
   */
  String url();

  /**
   * The path of the HTTP request
   * @return the path
   */
  String path();

  /**
   * The path parameters of the HTTP request
   * @return a Map of path parameters
   */
  Map pathParams();

  /**
   * The query parameters of the HTTP request
   * @return a Map of query parameters
   */
  Map queryParams();

  /**
   * A callback to deliver redirect URLs to the server framework
   * @param destURL
   */
  void willRedirect(String destURL);
}

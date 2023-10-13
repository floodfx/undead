package com.undead4j.handle.http;

import java.util.Map;

public interface RequestAdaptor {
  public Map sessionData();
  public String url();
  public String path();
  public Map pathParams();
  public void willRedirect(String destURL);
}

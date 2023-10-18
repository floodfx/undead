package com.undead4j.handle.http;

import java.util.Map;

public interface RequestAdaptor {
  Map sessionData();

  String url();

  String path();

  Map pathParams();

  void willRedirect(String destURL);
}

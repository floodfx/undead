package com.undead4j;

import com.undead4j.template.PageTemplate;
import com.undead4j.template.WrapperTemplate;
import com.undead4j.view.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Config {
  private Map<String, Class> viewRegistry = new ConcurrentHashMap<>();
  public PageTemplate pageTemplate;
  public WrapperTemplate wrapperTemplate;

  public Function<String, Class> routeMatcher;

  public void register(String path, Class view) {
    System.out.println("registering path:"+path+" class:"+view);
    var old = this.viewRegistry.put(path, view);
    if(old == null || !view.equals(old)) {
      System.out.printf("Warning: replaced %s view with %s for path %s", old, view, path);
    }
  }
  public View matchPath(String path) {
    var clazz = this.viewRegistry.get(path);
    System.out.println("looking up path:"+path+" found:"+clazz);
    if(clazz == null) {
      return null;
    }
    try {
      return (View)clazz.newInstance();
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
}

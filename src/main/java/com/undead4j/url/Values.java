package com.undead4j.url;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Values {

  private final Multimap<String, String> values;

  public Values() {
    this.values = ArrayListMultimap.create();
  }

  // create Values from map
  public static Values from(Map<String, String> map) {
    var v = new Values();
    for (var key : map.keySet()) {
      v.add(key, map.get(key));
    }
    return v;
  }

  public static Values from(String urlEncoded) {
    var values = new Values();
    // URLEncodedUtils doesn't like query strings without a leading "?"
    if (!urlEncoded.startsWith("?")) {
      urlEncoded = "?" + urlEncoded;
    }
    try {
      // URLEncodedUtils handles spaces and "+" correctly whereas URLDecoder does not
      List<NameValuePair> params = URLEncodedUtils.parse(new URI(urlEncoded), StandardCharsets.UTF_8);
      // iterate over params creating a Map<String,Object> handling multiple values for the same key
      var map = new HashMap<String, Object>();
      for (NameValuePair param : params) {
        values.add(param.getName(), param.getValue());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return values;
  }

  public void add(String key, String value) {
    this.values.put(key, value);
  }

  public void delete(String key) {
    this.values.removeAll(key);
  }

  public void has(String key) {
    this.values.containsKey(key);
  }

  public void set(String key, Object value) {
    this.values.removeAll(key);
    switch (value) {
      case String s -> this.values.put(key, s);
      case List l -> {
        for (Object o : l) {
          this.values.put(key, String.valueOf(o));
        }
      }
      // throw error?
      default -> this.values.put(key, String.valueOf(value));
    }
  }

  public String get(String key) {
    var v = this.values.get(key);
    if (v.size() > 0) {
      return v.iterator().next();
    }
    return null;
  }

  public List<String> getAll(String key) {
    var v = this.values.get(key);
    return List.copyOf(v);
  }

  public String toString() {
    return this.values.toString();
  }

  public Map<String, Object> asMap() {
    // convert to Map<String, Object> where object is either a String or List<String>
    // depending on if there are multiple values for the same key or not
    var map = new HashMap<String, Object>();
    for (var key : this.values.keySet()) {
      if (this.values.get(key).size() == 1) {
        map.put(key, this.values.get(key).iterator().next());
        continue;
      }
      map.put(key, List.copyOf(this.values.get(key)));
    }
    return map;
  }
}

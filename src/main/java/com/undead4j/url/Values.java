package com.undead4j.url;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.net.URLDecoder;
import java.util.Map;

public class Values {

  // create Values from map
  public static Values from(Map<String, String> map) {
    var v = new Values();
    for (var key : map.keySet()) {
      v.add(key, map.get(key));
    }
    return v;
  }

  public static Values from(String query) {
    var values = new Values();
    var pairs = query.split("&");
    for (var pair : pairs) {
      var kv = pair.split("=");
      if (kv.length > 1) {
        // TODO url decode
        try {
        var val = URLDecoder.decode(kv[1].replace("+", "%2B"), "UTF-8")
            .replace("%2B", "+");

        values.add(kv[0], val);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    return values;
  }

  private Multimap<String, String> values;
  public Values() {
    this.values = ArrayListMultimap.create();
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

  public String get(String key) {
    var v = this.values.get(key);
    if (v.size() > 0) {
      return v.iterator().next();
    }
    return null;
  }

  public String[] getAll(String key) {
    var v = this.values.get(key);
    return v.toArray(new String[v.size()]);
  }

  public String toString() {
    return this.values.toString();
  }
}

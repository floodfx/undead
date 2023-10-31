package run.undead.url;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;
import run.undead.event.UndeadEvent;
import run.undead.js.JS;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Values is a convenience wrapper around Multimap<String,String>.  It represents
 * the data associated with an {@link UndeadEvent} including events sent via
 * {@link JS#push} along with query string parameters and form data.
 */
public class Values {

  private final Multimap<String, String> values;

  public Values() {
    this.values = ArrayListMultimap.create();
  }

  /**
   * from creates a Values object from a Map<String,String>
   * @param map the map to create the Values object from
   * @return a Values object
   */
  public static Values from(Map<String, String> map) {
    var v = new Values();
    for (var key : map.keySet()) {
      v.add(key, map.get(key));
    }
    return v;
  }

  /**
   * from creates a Values object from a url encoded string
   * @param urlEncoded the encoded string to create the Values object from
   * @return a Values object
   */
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

  /**
   * add adds a key/value pair to the Values object
   * @param key the key to add
   * @param value the value to add
   */
  public void add(String key, String value) {
    this.values.put(key, value);
  }

  /**
   * remove removes a key/value pair from the Values object
   * @param key the key to remove
   */
  public void delete(String key) {
    this.values.removeAll(key);
  }

  /**
   * has returns true if the Values object contains the key
   * @param key the key to check
   */
  public void has(String key) {
    this.values.containsKey(key);
  }

  /**
   * set sets the value for the given key.  If the value is a String, it is set as is.  If the value is a List,
   * each value in the list is set.  Otherwise, the value is converted to a String and set.
   * @param key the key to set
   * @param value the value to set
   */
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

  /**
   * get returns the first value for the given key or null if the key does not exist
   * @param key the key to get
   * @return the first value for the given key or null if the key does not exist
   */
  public String get(String key) {
    var v = this.values.get(key);
    if (v.size() > 0) {
      return v.iterator().next();
    }
    return null;
  }

  /**
   * getAll returns all values for the given key or an empty list if the key does not exist
   * @param key the key to get
   * @return all values for the given key or an empty list if the key does not exist
   */
  public List<String> getAll(String key) {
    var v = this.values.get(key);
    return List.copyOf(v);
  }

  // TODO
//  public String urlEncode() {
//  }

  /**
   * asMap returns the Values object as a Map<String,Object> where Object is either a String or List<String>
   * @return the Values object as a Map<String,Object>
   */
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

  /**
   * toString returns the keys and values in the Values
   * Note: this is not suitable for use in a query string
   * @return the Values object as a url encoded string
   */
  public String toString() {
    return this.values.toString();
  }
}

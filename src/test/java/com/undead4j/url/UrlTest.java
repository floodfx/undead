package com.undead4j.url;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlTest {

  @Test
  public void TestValuesWithURLEncoded() {
    var myName = URLEncoder.encode("my name", StandardCharsets.UTF_8);
    var myVal = URLDecoder.decode(myName, StandardCharsets.UTF_8);
    System.out.println("myName:" + myName +" myVal:" + myVal);
    var values = Values.from("name=my+name&name=foo&age=24&_target=name");
    assertEquals(List.of("my name", "foo"), values.getAll("name"));
    assertEquals("24", values.get("age"));
    assertEquals("name", values.get("_target"));

    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    var foo = mapper.convertValue(values.asMap(), Foo.class);
    assertEquals(foo.name, List.of("my name", "foo"));
    assertEquals(foo.age, 24);
  }

  @Test
  public void ParseURLEncoded() throws java.net.URISyntaxException{
    String bodyStr = "?name=my+name&name=foo&age=24&_target=name";
    List<NameValuePair> params = URLEncodedUtils.parse(new URI(bodyStr), StandardCharsets.UTF_8);
    // iterate over params creating a Map<String,Object> handling multiple values for the same key
    var map = new HashMap<String, Object>();
    for (NameValuePair param : params) {
      if (map.containsKey(param.getName())) {
        var v = map.get(param.getName());
        if (v instanceof List) {
          ((List) v).add(param.getValue());
        } else {
          var l = List.of(v, param.getValue());
          map.put(param.getName(), l);
        }
      } else {
        map.put(param.getName(), param.getValue());
      }
    }
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    var foo = mapper.convertValue(map, Foo.class);
    assertEquals(foo.name, List.of("my name", "foo"));
    assertEquals(foo.age, 24);
  }
}

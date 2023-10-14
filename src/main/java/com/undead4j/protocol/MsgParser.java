package com.undead4j.protocol;

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MsgParser {
  public Msg parseJSON(String json) throws IOException, JsonDataException {
    System.out.println("parsing:" + json);
    var moshi = new Moshi.Builder().build();
    var jsonAdapter = moshi.adapter(List.class);

    var raw = jsonAdapter.fromJson(json);
    if (raw.size() != 5) {
      throw new JsonDataException("Invalid format: should be json array with 5 elements");
    }
    var joinRef = raw.get(0);
    var msgRef = raw.get(1);
    var topic = raw.get(2);
    var event = raw.get(3);
    var payload = raw.get(4);

    if(joinRef != null) {
      switch (joinRef) {
        case String s -> {
        }
        default -> throw new JsonDataException("joinRef must be a string");
      }
    }
    switch (msgRef) {
      case String s -> {
      }
      default -> throw new JsonDataException("msgRef must be a string");
    }
    switch (topic) {
      case String s -> {
      }
      default -> throw new JsonDataException("topic must be a string");
    }
    switch (event) {
      case String s -> {
      }
      default -> throw new JsonDataException("event must be a string");
    }
    switch (payload) {
      case Map m -> {
      }
      default -> throw new JsonDataException("payload must be a map");
    }

    return new Msg(
        (String) joinRef, (String) msgRef, (String) topic, (String) event, (Map) payload);
  }
}

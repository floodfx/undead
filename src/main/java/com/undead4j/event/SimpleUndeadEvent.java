package com.undead4j.event;

import com.undead4j.url.Values;

public class SimpleUndeadEvent implements UndeadEvent {
  private String type;
  private Values data;
  public SimpleUndeadEvent(String type, Values data) {
    this.type = type;
    this.data = data;
  }
  @Override
  public String type() {
    return this.type;
  }
  @Override
  public Values data() {
    return this.data;
  }
}

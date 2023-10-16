package com.undead4j.event;

import java.util.Map;

public class SimpleUndeadInfo implements UndeadInfo {
  private String type;
  private Map data;
  public SimpleUndeadInfo(String type, Map data) {
    this.type = type;
    this.data = data;
  }
  @Override
  public String type() {
    return this.type;
  }
  @Override
  public Map data() {
    return this.data;
  }
}

package com.undead4j.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * SetAttrOpts are the options for the set_attr command. This command
 * sets an attribute on the targeted element.
 */
public class SetAttrOpts implements Cmd {

  protected String to;
  protected String attr;
  protected String value;

  /**
   * sets the attribute on the current element
   * @param attr the attribute to set
   * @param value the value to set the attribute to
   */
  public SetAttrOpts(String attr, String value) {
    this(attr, value, null);
  }

  /**
   * sets the attribute on the element specified by the DOM selector
   * @param attr the attribute to set
   * @param value the value to set the attribute to
   * @param to the DOM selector for the targeted element
   */
  public SetAttrOpts(String attr, String value, String to) {
    this.to = to;
    this.attr = attr;
    this.value = value;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(SetAttrOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * SetAttrCmdAdapter is a Moshi adaptor for the SetAttrOpts class.
 */
class SetAttrCmdAdapter {
  @ToJson
  public List<Object> toJSON(SetAttrOpts cmd) {
    var attr = new ArrayList<>();
    attr.add(cmd.attr);
    attr.add(cmd.value);
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    map.put("attr", attr);
    var list = new ArrayList<>();
    list.add("set_attr");
    list.add(map);
    return list;
  }

}

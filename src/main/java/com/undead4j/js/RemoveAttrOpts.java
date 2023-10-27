package com.undead4j.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * RemoveAttrOpts are the options for the remove_attr command. This command
 * removes an attribute from the targeted element.
 */
public class RemoveAttrOpts implements Cmd {

  protected String to;
  protected String attr;

  /**
   * removes the attribute from the current element
   * @param attr the attribute to remove
   */
  public RemoveAttrOpts(String attr) {
    this(attr, null);
  }

  /**
   * removes the attribute from the element specified by the DOM selector
   * @param attr the attribute to remove
   * @param to the DOM selector for the targeted element
   */
  public RemoveAttrOpts(String attr, String to) {
    this.to = to;
    this.attr = attr;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(RemoveAttrOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * RemoveAttrCmdAdapter is a Moshi adaptor for the RemoveAttrOpts class.
 */
class RemoveAttrCmdAdapter {
  @ToJson
  public List<Object> toJSON(RemoveAttrOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    map.put("attr", cmd.attr);
    var list = new ArrayList<>();
    list.add("remove_attr");
    list.add(map);
    return list;
  }

}

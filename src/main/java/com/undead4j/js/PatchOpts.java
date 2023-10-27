package com.undead4j.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * PatchOpts are the options for the patch JS command. This command
 * sends a patch event to the server and updates the browser's pushState history
 */
public class PatchOpts implements Cmd {

  protected Boolean replace;
  protected String href;

  /**
   * sends a patch event with given href to the server
   * @param href the href to send to the server
   */
  public PatchOpts(String href) {
    this(href, null);
  }

  /**
   * sends a patch event with given href to the server
   * @param href the href to send to the server
   * @param replace whether to replace the current history entry or not
   */
  public PatchOpts(String href, Boolean replace) {
    this.href = href;
    this.replace = replace;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(PatchOpts.class).toJson(this);
  }

}

/**
 * PatchCmdAdaptor is a Moshi adaptor for the PatchOpts class.
 */
class PatchCmdAdaptor {
  @ToJson
  public List<Object> toJSON(PatchOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("href", cmd.href);
    if (cmd.replace != null){
      map.put("replace", cmd.replace);
    }
    var list = new ArrayList<>();
    list.add("patch");
    list.add(map);
    return list;
  }

}

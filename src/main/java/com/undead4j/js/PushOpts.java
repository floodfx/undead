package com.undead4j.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PushOpts are the options for the push command. This command
 * pushes an event to the server.
 */
public class PushOpts implements Cmd {

  protected String event;
  protected String target;
  protected String loading;
  protected Boolean pageLoading;
  protected Map values;

  /**
   * Pushes an event to the server.
   * @param event the event to push
   */
  public PushOpts(String event) {
    this(event, null, null, null, null);
  }

  /**
   * Pushes an event to the server with a target.
   * @param event the event to push
   * @param target the DOM selector for the targeted element
   */
  public PushOpts(String event, String target) {
    this(event, target, null, null, null);
  }

  /**
   * Pushes an event to the server with a target and loading indicator.
   * @param event the event to push
   * @param target the DOM selector for the targeted element
   * @param loading the DOM selector to apply the loading indicator to
   */
  public PushOpts(String event, String target, String loading) {
    this(event, target, loading, null, null);
  }

  /**
   * Pushes an event to the server with a target, loading indicator, and page loading indicator.
   * @param event the event to push
   * @param target the DOM selector for the targeted element
   * @param loading the DOM selector to apply the loading indicator to
   * @param pageLoading whether or not to trigger page loading events
   */
  public PushOpts(String event, String target,String loading, Boolean pageLoading) {
    this(event, target, loading, pageLoading, null);
  }

  /**
   * Pushes an event to the server with a target, loading indicator, page loading indicator, and values.
   * @param event the event to push
   * @param target the DOM selector for the targeted element
   * @param loading the DOM selector to apply the loading indicator to
   * @param pageLoading whether or not to trigger page loading events
   * @param values the values to include with the event sent to the server
   */
  public PushOpts(String event, String target, String loading, Boolean pageLoading, Map values) {
    this.event = event;
    this.target = target;
    this.loading = loading;
    this.pageLoading = pageLoading;
    this.values = values;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(PushOpts.class).toJson(this);
  }

}

/**
 * PushCmdAdapter is a Moshi adaptor for the PushOpts class.
 */
class PushCmdAdapter {
  @ToJson
  public List toJSON(PushOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("event", cmd.event);
    // only add additional props if not null
    if(cmd.target != null) {
      map.put("target", cmd.target);
    }
    if(cmd.loading != null) {
      map.put("loading", cmd.loading);
    }
    if(cmd.pageLoading != null) {
      map.put("page_loading", cmd.pageLoading);
    }
    if(cmd.values != null) {
      map.put("value", cmd.values);
    }

    var list = new ArrayList<>();
    list.add("push");
    list.add(map);
    return list;
  }

}

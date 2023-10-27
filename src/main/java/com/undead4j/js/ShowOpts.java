package com.undead4j.js;

import com.squareup.moshi.ToJson;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ShowOpts are the options for the show JS command. This command
 * shows the targeted element.
 */
public class ShowOpts implements Cmd {
  protected String to;
  protected Long time;
  protected Transition transition;
  protected String display;

  /**
   * shows the current element
   */
  public ShowOpts() {
    this(null, Duration.ofMillis(200), new Transition(), JS.DEFAULT_DISPLAY);
  }

  /**
   * shows the element specified by the DOM selector
   * @param to the DOM selector for the targeted element
   */
  public ShowOpts(String to) {
    this(to, Duration.ofMillis(200), new Transition(), JS.DEFAULT_DISPLAY);
  }

  /**
   * shows the element specified by the DOM selector and
   * a duration for the transition.
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   */
  public ShowOpts(String to, Duration time) {
    this(to, time, new Transition(), JS.DEFAULT_DISPLAY);
  }

  /**
   * shows the element specified by the DOM selector, a
   * duration for the transition, and a {@link Transition}
   * containing the css transition classes to apply.
   * @see Transition
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param transition the css transition classes to apply
   */
  public ShowOpts(String to, Duration time, Transition transition) {
    this(to, time, transition, JS.DEFAULT_DISPLAY);
  }

  /**
   * shows the element specified by the DOM selector, a
   * duration for the transition, and a {@link Transition}
   * containing the css transition classes to apply.
   * @see Transition
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param transition the css transition classes to apply
   * @param display the css display property to apply
   */
  public ShowOpts(String to, Duration time, Transition transition, String display) {
    this.to = to;
    this.time = time.toMillis();
    this.transition = transition;
    this.display = display;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(ShowOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * ShowCmdAdapter is a Moshi adaptor for the ShowOpts class.
 */
class ShowCmdAdapter {
  @ToJson
  public List toJSON(ShowOpts showOpts) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", showOpts.to);
    map.put("time", showOpts.time);
    map.put("transition", showOpts.transition);
    map.put("display", showOpts.display);
    var list = new ArrayList<>();
    list.add("show");
    list.add(map);
    return list;
  }

}

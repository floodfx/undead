package run.undead.js;

import com.squareup.moshi.ToJson;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ToggleOpts are the options for the toggle JS command. This command
 * toggles the visibility of the targeted element.
 */
public class ToggleOpts implements Cmd {

  protected String to;
  protected Long time;
  protected Transition ins;
  protected Transition outs;
  protected String display;

  /**
   * toggles the visibility of the current element
   */
  public ToggleOpts() {
    this(null, Duration.ofMillis(200), new Transition(), new Transition(), JS.DEFAULT_DISPLAY);
  }

  /**
   * toggles the visibility of the element specified by the DOM selector
   * @param to the DOM selector for the targeted element
   */
  public ToggleOpts(String to) {
    this(to, Duration.ofMillis(200), new Transition(), new Transition(), JS.DEFAULT_DISPLAY);
  }

  /**
   * toggles the visibility of the element specified by the DOM selector and
   * a duration for the transition.
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   */
  public ToggleOpts(String to, Duration time) {
    this(to, time, new Transition(), new Transition(), JS.DEFAULT_DISPLAY);
  }

  /**
   * toggles the visibility of the element specified by the DOM selector, a
   * duration for the transition, and a {@link Transition}
   * containing the css transition classes to apply when showing the element.
   * @see Transition
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param in the css transition classes to apply when showing the element
   */
  public ToggleOpts(String to, Duration time, Transition in) {
    this(to, time, in, new Transition(), JS.DEFAULT_DISPLAY);
  }

  /**
   * toggles the visibility of the element specified by the DOM selector, a
   * duration for the transition, a {@link Transition}
   * containing the css transition classes to apply when showing the element
   * and a {@link Transition} containing the css transition classes to apply
   * when hiding the element.
   * @see Transition
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param in the css transition classes to apply when showing the element
   * @param out the css transition classes to apply when hiding the element
   */
  public ToggleOpts(String to, Duration time, Transition in, Transition out) {
    this(to, time, in, out, JS.DEFAULT_DISPLAY);
  }

  /**
   * toggles the visibility of the element specified by the DOM selector, a
   * duration for the transition, a {@link Transition}
   * containing the css transition classes to apply when showing the element,
   * a {@link Transition} containing the css transition classes to apply
   * when hiding the element and a css display property to apply when showing
   * the element.
   * @see Transition
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param in the css transition classes to apply when showing the element
   * @param out the css transition classes to apply when hiding the element
   * @param display the css display property to apply
   */
  public ToggleOpts(String to, Duration time, Transition in, Transition out, String display) {
    this.to = to;
    this.time = time.toMillis();
    this.ins = in;
    this.outs = out;
    this.display = display;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(ToggleOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * ToggleCmdAdapter is a Moshi adaptor for the ToggleOpts class.
 */
class ToggleCmdAdapter {
  @ToJson
  public List toJSON(ToggleOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    map.put("time", cmd.time);
    map.put("ins", cmd.ins);
    map.put("outs", cmd.outs);
    map.put("display", cmd.display);
    var list = new ArrayList<>();
    list.add("toggle");
    list.add(map);
    return list;
  }

}

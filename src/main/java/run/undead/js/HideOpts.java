package run.undead.js;

import com.squareup.moshi.ToJson;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * HideOpts are the options for the hide JS command. This command
 * hides the targeted element.
 */
public class HideOpts implements Cmd {
  protected String to;
  protected Long time;
  protected Transition transition;

  /**
   * hides the current element
   */
  public HideOpts() {
    this(null, Duration.ofMillis(200), new Transition());
  }

  /**
   * hides the element specified by the DOM selector
   * @param to the DOM selector for the targeted element
   */
  public HideOpts(String to) {
    this(to, Duration.ofMillis(200), new Transition());
  }

  /**
   * hides the element specified by the DOM selector and
   * a duration for the transition.
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   */
  public HideOpts(String to, Duration time) {
    this(to, time, new Transition());
  }

  /**
   * hides the element specified by the DOM selector, a
   * duration for the transition, and a {@link Transition}
   * containing the css transition classes to apply.
   * @see Transition
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param transition the css transition classes to apply
   */
  public HideOpts(String to, Duration time, Transition transition) {
    this.to = to;
    this.time = time.toMillis();
    this.transition = transition;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(HideOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * HideCmdAdapter is a Moshi adaptor for the HideOpts class.
 */
class HideCmdAdapter {
  @ToJson
  public List toJSON(HideOpts hideOpts) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", hideOpts.to);
    map.put("time", hideOpts.time);
    map.put("transition", hideOpts.transition);
    var list = new ArrayList<>();
    list.add("hide");
    list.add(map);
    return list;
  }

}

package run.undead.js;

import com.squareup.moshi.ToJson;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * TransitionOpts are the options for the transition JS command. This command
 * transitions the targeted element.
 */
public class TransitionOpts implements Cmd {
  protected String to;
  protected Long time;
  protected Transition transition;

  /**
   * transitions the current element
   * @param transition the transition to apply
   */
  public TransitionOpts(Transition transition) {
    this(transition, null, Duration.ofMillis(200));
  }

  /**
   * transitions the element specified by the DOM selector
   * @param transition the transition to apply
   * @param to the DOM selector for the targeted element
   */
  public TransitionOpts(Transition transition, String to) {
    this(transition, to, Duration.ofMillis(200));
  }

  /**
   * transitions the element specified by the DOM selector and
   * a duration for the transition.
   * @param transition the transition to apply
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   */
  public TransitionOpts(Transition transition, String to, Duration time) {
    this.to = to;
    this.time = time.toMillis();
    this.transition = transition;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(TransitionOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * TransitionCmdAdapter is a Moshi adaptor for the TransitionOpts class.
 */
class TransitionCmdAdapter {
  @ToJson
  public List toJSON(TransitionOpts transitionOpts) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", transitionOpts.to);
    map.put("time", transitionOpts.time);
    map.put("transition", transitionOpts.transition);
    var list = new ArrayList<>();
    list.add("transition");
    list.add(map);
    return list;
  }

}

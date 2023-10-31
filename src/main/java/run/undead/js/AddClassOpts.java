package run.undead.js;

import com.squareup.moshi.ToJson;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * AddClassOpts are the options for the add_class JS command.
 * @see JS#addClass(AddClassOpts)
 * @see JS#addClass(String)
 */
public class AddClassOpts implements Cmd {
  protected String classNames;

  protected String to;
  protected Long time;
  protected Transition transition;

  /**
   * AddClassOpts accepts css class names and adds them to element being targeted
   * @param classNames the css class names (space separated)
   */
  public AddClassOpts(String classNames) {
    this(classNames,null, Duration.ofMillis(200), new Transition());
  }

  /**
   * AddClassOpts accepts css class names and a DOM selector to apply them to
   * @param classNames the css class names (space separated)
   * @param to the DOM selector for the targeted element
   */
  public AddClassOpts(String classNames, String to) {
    this(classNames, to, Duration.ofMillis(200), new Transition());
  }

  /**
   * AddClassOpts accepts css class names, a DOM selector to apply them to, and
   * a duration for the transition.
   * @param classNames the css class names (space separated)
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   */
  public AddClassOpts(String classNames, String to, Duration time) {
    this(classNames, to, time, new Transition());
  }

  /**
   * AddClassOpts accepts css class names, a DOM selector to apply them to, a
   * duration for the transition, and a {@link Transition} containing the
   * css transition classes to apply.
   * @see Transition
   * @param classNames the css class names (space separated)
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param transition the css transition classes to apply
   */
  public AddClassOpts(String classNames, String to, Duration time, Transition transition) {
    this.classNames = classNames;
    this.to = to;
    this.time = time.toMillis();
    this.transition = transition;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(AddClassOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * AddClassCmdAdaptor is responsible for serializing AddClassOpts to JSON.
 */
class AddClassCmdAdapter {
  @ToJson
  public List toJSON(AddClassOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("names", cmd.classNames.split("\s+"));
    map.put("to", cmd.to);
    map.put("time", cmd.time);
    map.put("transition", cmd.transition);
    var list = new ArrayList<>();
    list.add("add_class");
    list.add(map);
    return list;
  }

}

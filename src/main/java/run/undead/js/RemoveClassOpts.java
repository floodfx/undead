package run.undead.js;

import com.squareup.moshi.ToJson;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * RemoveClassOpts are the options for the remove_class JS command. This command
 * removes the specified classes from the targeted element.
 */
public class RemoveClassOpts implements Cmd {
  protected String classNames;

  protected String to;
  protected Long time;
  protected Transition transition;

  /**
   * removes the specified classes from the current element
   * @param classNames the classes to remove
   */
  public RemoveClassOpts(String classNames) {
    this(classNames,null, Duration.ofMillis(200), new Transition());
  }

  /**
   * removes the specified classes from the current element
   * @param classNames the classes to remove
   * @param to the DOM selector for the targeted element
   */
  public RemoveClassOpts(String classNames, String to) {
    this(classNames, to, Duration.ofMillis(200), new Transition());
  }

  /**
   * removes the specified classes from the current element
   * @param classNames the classes to remove
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   */
  public RemoveClassOpts(String classNames, String to, Duration time) {
    this(classNames, to, time, new Transition());
  }

  /**
   * removes the specified classes from the current element
   * @param classNames the classes to remove
   * @param to the DOM selector for the targeted element
   * @param time the duration of the transition
   * @param transition the css transition classes to apply
   *                   @see Transition
   */
  public RemoveClassOpts(String classNames, String to, Duration time, Transition transition) {
    this.classNames = classNames;
    this.to = to;
    this.time = time.toMillis();
    this.transition = transition;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(RemoveClassOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * RemoveClassCmdAdapter is a Moshi adaptor for the RemoveClassOpts class.
 */
class RemoveClassCmdAdapter {
  @ToJson
  public List toJSON(RemoveClassOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("names", cmd.classNames.split("\s+"));
    map.put("to", cmd.to);
    map.put("time", cmd.time);
    map.put("transition", cmd.transition);
    var list = new ArrayList<>();
    list.add("remove_class");
    list.add(map);
    return list;
  }

}

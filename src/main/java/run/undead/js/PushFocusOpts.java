package run.undead.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * PushFocusOpts are the options for the push_focus command. This command
 * pushes focus from the source element to be later popped with pop_focus.
 */
public class PushFocusOpts implements Cmd {

  protected String to;

  /**
   * Pushes focus from the current element to be later popped with pop_focus.
   */
  public PushFocusOpts() {
    this(null);
  }

  /**
   * Pushes focus from the current element to the element specified by the DOM selector
   * to be later popped with pop_focus.
   * @param to the DOM selector for the targeted element
   */
  public PushFocusOpts(String to) {
    this.to = to;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(PushFocusOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * PushFocusCmdAdaptor is a Moshi adaptor for the PushFocusOpts class.
 */
class PushFocusCmdAdaptor {
  @ToJson
  public List<Object> toJSON(PushFocusOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    var list = new ArrayList<>();
    list.add("push_focus");
    list.add(map);
    return list;
  }

}

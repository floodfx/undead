package run.undead.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * FocusOpts are the options for the focus command.
 * @see JS#focus(String)
 * @see JS#focus()
 */
public class FocusOpts implements Cmd {

  protected String to;

  /**
   * focus on current element
   */
  public FocusOpts() {
    this(null);
  }

  /**
   * focus on the element specified by the DOM selector
   * @param to the DOM selector for the targeted element
   */
  public FocusOpts(String to) {
    this.to = to;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(FocusOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * FocusCmdAdaptor is a Moshi adaptor for the FocusOpts class.
 */
class FocusCmdAdaptor {
  @ToJson
  public List<Object> toJSON(FocusOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    var list = new ArrayList<>();
    list.add("focus");
    list.add(map);
    return list;
  }

}

package run.undead.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * FocusFirstOpts are the options for the focus_first command.
 * @see JS#focusFirst(FocusFirstOpts)
 * @see JS#focusFirst()
 */
public class FocusFirstOpts implements Cmd {

  protected String to;

  /**
   * Applies focus_first to current element
   */
  public FocusFirstOpts() {
    this(null);
  }

  /**
   * Applies focus_first to the element specified by the DOM selector
   * @param to the DOM selector for the targeted element
   */
  public FocusFirstOpts(String to) {
    this.to = to;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(FocusFirstOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * FocusFirstCmdAdaptor is a Moshi adaptor for the FocusFirstOpts class.
 */
class FocusFirstCmdAdaptor {
  @ToJson
  public List<Object> toJSON(FocusFirstOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    var list = new ArrayList<>();
    list.add("focus_first");
    list.add(map);
    return list;
  }

}

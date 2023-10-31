package run.undead.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ExecOpts are the options for the exec JS command.
 */
public class ExecOpts implements Cmd {

  protected String to;
  protected String attr;

  /**
   * ExecOpts takes the target attribute name that contains the JS Commands to execute.
   * @param attr the target attribute name that contains the JS Commands to execute
   */
  public ExecOpts(String attr) {
    this(attr, null);
  }

  /**
   * ExecOpts takes the target attribute name that contains the JS Commands to execute
   * and a DOM selector that contains the attribute.
   * @param attr the target attribute name that contains the JS Commands to execute
   * @param to the DOM selector that contains the attribute
   */
  public ExecOpts(String attr, String to) {
    this.to = to;
    this.attr = attr;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(ExecOpts.class).serializeNulls().toJson(this);
  }

}

/**
 * ExecCmdAdaptor is a Moshi adaptor for the ExecOpts class.
 */
class ExecCmdAdaptor {
  @ToJson
  public List<Object> toJSON(ExecOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    map.put("attr", cmd.attr);
    var list = new ArrayList<>();
    list.add("exec");
    list.add(map);
    return list;
  }

}

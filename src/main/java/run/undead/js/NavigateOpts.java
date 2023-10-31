package run.undead.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * NavigateOpts are the options for the navigate JS command. This command
 * sends a navigation event to the server and updates the browser's pushState history
 */
public class NavigateOpts implements Cmd {

  protected Boolean replace;
  protected String href;

  /**
   * sends a navigation event with given href to the server
   * @param href the URL to navigate to
   */
  public NavigateOpts(String href) {
    this(href, null);
  }

  /**
   * sends a navigation event with given href to the server
   * @param href the URL to navigate to
   * @param replace whether to replace the current history entry or not
   */
  public NavigateOpts(String href, Boolean replace) {
    this.href = href;
    this.replace = replace;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(NavigateOpts.class).toJson(this);
  }

}

/**
 * NavigateCmdAdaptor is a Moshi adaptor for the NavigateOpts class.
 */
class NavigateCmdAdaptor {
  @ToJson
  public List<Object> toJSON(NavigateOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("href", cmd.href);
    if(cmd.replace != null) {
      map.put("replace", cmd.replace);
    }
    var list = new ArrayList<>();
    list.add("navigate");
    list.add(map);
    return list;
  }

}

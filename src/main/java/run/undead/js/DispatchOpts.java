package run.undead.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DispatchOpts are the options for the dispatch JS command.
 * @see JS#dispatch(DispatchOpts)
 * @see JS#dispatch(String)
 */
public class DispatchOpts implements Cmd {
  protected final String to;
  protected final String event;
  protected final Map<String, Object> detail;
  protected final Boolean bubbles;

  /**
   * DispatchOpts accepts the name of the event to dispatch to the DOM
   * @param event the name of the event to dispatch
   */
  public DispatchOpts(String event) {
    this(event, null, null, null);
  }

  /**
   * DispatchOpts accepts the name of the event to dispatch to the DOM
   * and the DOM selector to dispatch it to
   * @param event the name of the event to dispatch
   * @param to the DOM selector to dispatch the event to
   */
  public DispatchOpts(String event, String to) {
    this(event, to, null, null);
  }

  /**
   * DispatchOpts accepts the name of the event to dispatch to the DOM, the DOM selector
   * to dispatch it to, and a map that sets the detail of the event. (Client
   * side events have an optional payload named `detail` that can be set which is
   * what is set by the provided Map)
   * @param event the name of the event to dispatch
   * @param to the DOM selector to dispatch the event to
   * @param detail the detail of the event
   */
  public DispatchOpts(String event, String to, Map<String, Object> detail) {
    this(event, to, detail, null);
  }

  /**
   * DispatchOpts accepts the name of the event to dispatch to the DOM, the DOM selector
   * the DOM selector to dispatch it to, and a map that sets the detail of the event,
   * and a flag to control whether the event bubbles up the DOM tree or not.
   * @param event the name of the event to dispatch
   * @param to the DOM selector to dispatch the event to
   * @param detail the detail of the event
   * @param bubbles whether the event bubbles up the DOM tree or not
   */
  public DispatchOpts(String event, String to, Map<String, Object> detail, Boolean bubbles) {
    this.to = to;
    this.event = event;
    this.detail = detail;
    this.bubbles = bubbles == null ? true : bubbles;
  }

  @Override
  public String toJSON() {
    return JS.moshi.adapter(DispatchOpts.class).serializeNulls().toJson(this);
  }
}

/**
 * DispatchCmdAdapter is the Moshi adapter for DispatchOpts
 */
class DispatchCmdAdapter {
  @ToJson
  public List<Object> toJSON(DispatchOpts cmd) {
    var map = new LinkedHashMap<>(); // preserve order
    map.put("to", cmd.to);
    map.put("event", cmd.event);
    // only add detail if it's not null
    if( cmd.detail != null ) {
      map.put("detail", cmd.detail);
    }
    // only add bubbles if it's false
    if( !cmd.bubbles ) {
      map.put("bubbles", false);
    }
    var list = new ArrayList<>();
    list.add("dispatch");
    list.add(map);
    return list;
  }
}

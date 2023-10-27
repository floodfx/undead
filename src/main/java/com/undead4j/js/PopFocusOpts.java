package com.undead4j.js;

import com.squareup.moshi.ToJson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * PopFocusOpts are the options for the pop_focus command. This command
 * pops the focus stack and focuses on the previous element.  There are no
 * options for this command but this class handles the serialization.
 */
public class PopFocusOpts implements Cmd {

  @Override
  public String toJSON() {
    return JS.moshi.adapter(PopFocusOpts.class).toJson(this);
  }

}

/**
 * PopFocusCmdAdaptor is a Moshi adaptor for the PopFocusOpts class.
 */
class PopFocusCmdAdaptor {
  @ToJson
  public List<Object> toJSON(PopFocusOpts cmd) {
    var list = new ArrayList<>();
    list.add("pop_focus");
    list.add(new LinkedHashMap<>());
    return list;
  }

}

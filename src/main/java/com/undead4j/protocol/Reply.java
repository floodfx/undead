package com.undead4j.protocol;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.undead4j.template.LiveTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Reply {
  private static final Moshi moshi = new Moshi.Builder().build();
  private static final JsonAdapter<List> listAdaptor = moshi.adapter(List.class);
  public static String NewRendered(Msg orig, LiveTemplate tmpl) {
    var data = List.of(
        orig.joinRef(),
        orig.msgRef(),
        orig.topic(),
        "phx_reply",
        Map.of(
            "status", "ok",
            "response", Map.of("rendered", tmpl.toParts())
        )
    );
    return listAdaptor.toJson(data);
  }

  public static String NewHeartbeat(Msg orig) {
    var data = new ArrayList();
    data.add(null);
    data.add(orig.msgRef());
    data.add("phoenix");
    data.add("phx_reply");
    data.add(Map.of("status", "ok"));
    return listAdaptor.toJson(data);
  }
}

package com.undead4j.protocol;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Reply {
  private static final Moshi moshi = new Moshi.Builder().build();
  private static final JsonAdapter<List> listAdaptor = moshi.adapter(List.class);
  public static String rendered(Msg orig, Map parts) {
    var data = List.of(
        orig.joinRef(),
        orig.msgRef(),
        orig.topic(),
        "phx_reply",
        Map.of(
            "status", "ok",
            "response", Map.of("rendered", parts)
        )
    );
    return listAdaptor.toJson(data);
  }

  public static String heartbeat(Msg orig) {
    var data = new ArrayList();
    data.add(null);
    data.add(orig.msgRef());
    data.add("phoenix");
    data.add("phx_reply");
    data.add(Map.of("status", "ok"));
    return listAdaptor.toJson(data);
  }

  public static String redirect(Msg orig, String url) {
    var data = new ArrayList();
    data.add(orig.joinRef());
    data.add(orig.msgRef());
    data.add(orig.topic());
    data.add("phx_reply");
    data.add(Map.of(
        "status", "ok",
        "response", Map.of(
            "to", url
        )
    ));
    return listAdaptor.toJson(data);
  }

  public static String replyDiff(Msg orig, Map parts)  {
    var data = new ArrayList();
    data.add(orig.joinRef());
    data.add(orig.msgRef());
    data.add(orig.topic());
    data.add("phx_reply");
    data.add(Map.of(
        "status", "ok",
        "response", Map.of(
            "diff", parts
        )
    ));
    return listAdaptor.toJson(data);
  }

  public static String diff(String topic, Map diff) {
    var data = new ArrayList();
    data.add(null);
    data.add(null); // empty msgRef
    data.add(topic);
    data.add("diff");
    data.add(diff);
    return listAdaptor.toJson(data);
  }
//
//  func NewDiff(joinRef *string, topic string, diff []byte) *Diff {
//    return &Diff{
//      JoinRef: joinRef,
//          Topic:   topic,
//          Event:   "diff",
//          Payload: diff,
//    }
//  }
}

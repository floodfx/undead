package com.undead4j.javalin;

import com.undead4j.socket.WsAdaptor;
import io.javalin.websocket.WsContext;

public class JavalinWsAdaptor implements WsAdaptor {
  private final WsContext ws;

  public JavalinWsAdaptor(WsContext ws) {
    this.ws = ws;
  }

  @Override
  public void send(String data) {
    ws.send(data);
  }
}

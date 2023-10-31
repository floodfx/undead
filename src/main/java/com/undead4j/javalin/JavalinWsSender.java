package com.undead4j.javalin;

import com.undead4j.context.WsSender;
import io.javalin.websocket.WsContext;

/**
 * JavalinWsSender is an implementation of {@link WsSender} for Javalin websockets.
 */
public class JavalinWsSender implements WsSender {
  private final WsContext ws;

  public JavalinWsSender(WsContext ws) {
    this.ws = ws;
  }

  @Override
  public void send(String data) {
    ws.send(data);
  }

}

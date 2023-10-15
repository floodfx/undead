package com.undead4j.socket;

/**
 * Interface to generalize communicating over a websocket for different implementations.
 */
public interface WsAdaptor {
  public void send(String data);
}

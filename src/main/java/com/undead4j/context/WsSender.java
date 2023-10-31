package com.undead4j.context;

/**
 * WsSender is an interface for sending String data to the websocket client.
 */
public interface WsSender {

  /**
   * Send string data to the client over the websocket.
   * @param data the data to send
   */
  void send(String data);
}

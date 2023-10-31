package com.undead4j.event;

import com.undead4j.context.Context;

import java.util.Map;

/**
 * UndeadInfo is the interface for all events that are from the server (either from a {@link Context#sendInfo(UndeadInfo)}
 * or from a pub/sub subscription.  Since this data comes directly from another server side process, Undead
 * does not parse it or otherwise modify it.  It is up to the developer to ensure that the data is valid and
 * the key/value pairs are correct.
 */
public interface UndeadInfo {

  /**
   * type is the type of event
   * @return the type of the event
   */
  String type();

  /**
   * data is the data associated with the event
   * @return a map of key/value pairs
   */
  Map data();
}

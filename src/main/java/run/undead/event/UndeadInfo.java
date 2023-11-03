package run.undead.event;

import run.undead.context.Context;

/**
 * UndeadInfo is the interface for all events that are from the server (either from a {@link Context#sendInfo(UndeadInfo)}
 * or from a pub/sub subscription.  Since this data comes directly from another server side process, Undead
 * does not parse it or otherwise modify.  It is up to the developer to parse the data and handle it appropriately.
 */
public interface UndeadInfo {

  /**
   * type is the type of info event
   * @return the type of the info event
   */
  String type();

  /**
   * data is the String data associated with the info
   * @return the String data
   */
  String data();
}

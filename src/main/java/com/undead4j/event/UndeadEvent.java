package com.undead4j.event;

import com.undead4j.url.Values;

/**
 * UndeadEvent is the interface for all events that are received from the browser.  Events are automatically
 * sent to the server when an element with a <code>ud-</code> attribute's event is triggered from an element
 * in an {@link com.undead4j.template.UndeadTemplate}.
 *
 * @see com.undead4j.template.UndeadTemplate
 * @see com.undead4j.view.View
 * @see com.undead4j.js.JS
 */
public interface UndeadEvent {
  /**
   * type is the type of the event.  For example, if a button has a <code>ud-click="my-event"</code> attribute,
   * then the type of the event will be <code>my-event</code>.
   * @return the type of the event
   */
  String type();

  /**
   * data contains the {@link Values} associated with the event.  For example, if a button has a
   * <code>ud-value-foo="bar"</code> attribute, then the data of the event will contain a key/value
   * pairs of <code>foo</code> to <code>bar</code>.
   * @return the data associated with the event
   */
  Values data();
}

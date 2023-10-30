package com.undead4j.event;

import com.undead4j.url.Values;

/**
 * SimpleUndeadEvent is a simple implementation of {@link UndeadEvent} that is used internally by Undead.
 * @param type the type of the event
 * @param data the data associated with the event
 */
public record SimpleUndeadEvent(String type, Values data) implements UndeadEvent {
}

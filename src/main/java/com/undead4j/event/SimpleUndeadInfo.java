package com.undead4j.event;

import java.util.Map;

/**
 * SimpleUndeadInfo is a simple implementation of {@link UndeadInfo} that is used internally by Undead.
 * @param type the type of the event
 * @param data the data associated with the event
 */
public record SimpleUndeadInfo(String type, Map data) implements UndeadInfo {
}

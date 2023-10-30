package com.undead4j.event;

import com.undead4j.url.Values;

public record SimpleUndeadEvent(String type, Values data) implements UndeadEvent {
}

package com.undead4j.event;

import com.undead4j.url.Values;

public interface UndeadEvent {
  String type();

  Values data();
}

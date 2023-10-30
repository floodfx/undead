package com.undead4j.event;

import java.util.Map;

public record SimpleUndeadInfo(String type, Map data) implements UndeadInfo {
}

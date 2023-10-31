package run.undead.protocol;

import java.util.Map;

public record Msg(
    String joinRef, String msgRef, String topic, String event, Map<String, Object> payload) {
}

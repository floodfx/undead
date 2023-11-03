package run.undead.event;

/**
 * SimpleUndeadInfo is a simple implementation of {@link UndeadInfo} that is used internally by Undead.
 * @param type the type of the event
 * @param data the String data associated with the event
 */
public record SimpleUndeadInfo(String type, String data) implements UndeadInfo {
}

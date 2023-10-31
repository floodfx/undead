package run.undead.js;

/**
 * Marker interface for JS commands that can be sent to the client.
 */
public interface Cmd {

  String toJSON();
}

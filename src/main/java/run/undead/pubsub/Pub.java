package run.undead.pubsub;

/**
 * Pub is an interface for publishing messages to a topic. It is extremely simple
 * on purpose and should be easy to implement for almost any pubsub system. The
 * String data could be JSON, base64 data, or many other formats.  The topic
 * could also be as simple or complex as needed.
 */
public interface Pub {
  /**
   * Publish a message to a topic.
   * @param topic the topic to publish to
   * @param data the data to publish
   */
  void publish(String topic, String data);
}

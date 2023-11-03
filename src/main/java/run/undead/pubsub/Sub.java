package run.undead.pubsub;

import java.util.function.BiConsumer;

/**
 * Sub is an interface for subscribing to a topic, receiving messages, and
 * unsubscribing. It is extremely simple on purpose and should be easy to
 * implement for almost any pubsub system.  Typically, you would subscribe
 * to a topic and provide a callback function that will be called when a
 * message is received.  The callback function will be passed the topic and
 * data.
 *
 * @see {@link Pub}
 */
public interface Sub {
  /**
   * Subscribe to a topic and provide a callback function that will be called
   * when a message is received.  The callback function will be passed the
   * topic and data.
   * @param topic the topic to subscribe to
   * @param callback the callback function to call when a message is received
   * @return a subscription id that can be used to {@link #unsubscribe(String)}
   */
  String subscribe(String topic, BiConsumer<String,String> callback);

  /**
   * Unsubscribe from a topic.
   * @param subscriptionId the subscription id returned from {@link #subscribe(String, BiConsumer)}
   */
  void unsubscribe(String subscriptionId);
}

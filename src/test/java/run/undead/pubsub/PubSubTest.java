package run.undead.pubsub;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PubSubTest {

  @Test
  public void testPublish() {
    var counter = new AtomicInteger(0);
    String subscriptionId = MemPubSub.INSTANCE.subscribe("my-topic", (topic, data) -> {
      assertEquals("my-topic", topic);
      assertEquals("my-data", data);
      counter.incrementAndGet();
    });

    // Publish a message to the topic
    MemPubSub.INSTANCE.publish("my-topic", "my-data");

    // Verify that the subscription callback is called
    assertEquals(1, counter.get());
  }

  @Test
  public void testUnsubscribe() {
    // Subscribe to a topic
    var counter = new AtomicInteger(0);
    String subscriptionId = MemPubSub.INSTANCE.subscribe("my-topic", (topic, data) -> {
      counter.incrementAndGet();
    });

    // Test that the subscription callback is called
    MemPubSub.INSTANCE.publish("my-topic", "my-data");
    assertEquals(1, counter.get());

    // now unsubscribe
    MemPubSub.INSTANCE.unsubscribe(subscriptionId);

    // Publish a message to the topic
    MemPubSub.INSTANCE.publish("my-topic", "my-data");

    // subscription callback should not be called
    assertEquals(1, counter.get());
  }
}


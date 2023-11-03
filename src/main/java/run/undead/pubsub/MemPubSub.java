package run.undead.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

/**
 * An in-memory pubsub implementation for development and testing.  This will not work
 * in a distributed environment.
 *
 * @see {@link PubSub}
 */
public enum MemPubSub implements PubSub {
  INSTANCE;

  private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
  private final Map<String, Map<String, BiConsumer<String, String>>> subs = new HashMap<>();

  @Override
  public void publish(String topic, String data) {
    Map<String, BiConsumer<String, String>> topicSubscriptions = subs.get(topic);
    if (topicSubscriptions != null) {
      var futureList = new ArrayList<Future>();
      for(BiConsumer<String, String> callback : topicSubscriptions.values()) {
        futureList.add(executorService.submit(() -> callback.accept(topic, data)));
      }
      for(Future future : futureList) {
        try {
          future.get();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public String subscribe(String topic, BiConsumer<String, String> callback) {
    String subscriptionId = UUID.randomUUID().toString();
    Map<String, BiConsumer<String, String>> topicSubscriptions = subs.computeIfAbsent(topic, k -> new HashMap<>());
    topicSubscriptions.put(subscriptionId, callback);
    return subscriptionId;
  }

  @Override
  public void unsubscribe(String subscriptionId) {
    for (Map<String, BiConsumer<String, String>> topicSubscriptions : subs.values()) {
      topicSubscriptions.remove(subscriptionId);
    }
  }
}

package kafkautils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.csx.protocol.protobuf.ProtobufMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaStreamingClient<T extends ProtobufMapper<?>> implements Closeable {

  private static final Logger log = LoggerFactory.getLogger(KafkaStreamingClient.class);

  private final Consumer<String, byte[]> consumer;
  private final String topic;
  private final Class<T> clazz;

  public KafkaStreamingClient(String topic, Class<T> clazz) {
    log.debug("Initializing KafkaStreamingClient...");
    this.consumer = new KafkaConsumer<>(KafkaProperties.getConsumerProperties());
    this.topic = topic;
    this.clazz = clazz;
    subscribeAndFindLatestOffset();
  }

  /**
   * see https://kafka.apache.org/24/javadoc/?org/apache/kafka/clients/consumer/KafkaConsumer.html
   */
  private void subscribeAndFindLatestOffset() {
    consumer.assign(Collections.singleton(new TopicPartition(topic, 0)));
    assertTrue(CollectionUtils.isNotEmpty(consumer.assignment()), "Consumer did not receive partition assignment for " + topic);
    consumer.assignment().forEach(partition -> log.info("Consumer assigned to partition {} at offset {}", partition, consumer.position(partition)));

    consumer.seekToEnd(consumer.assignment());
    consumer.poll(Duration.ofMillis(100)); // seekToEnd "evaluates lazily, seeking to the final offset in all partitions only when poll(Duration) is called"

    consumer.commitSync();
    consumer.assignment().forEach(partition -> log.info("seekToEnd yielded partition offset {}", consumer.position(partition)));
  }

  public long getLatestOffset() {
    TopicPartition partition = new TopicPartition(topic, 0);
    consumer.assign(Collections.singleton(partition));
    assertTrue(CollectionUtils.isNotEmpty(consumer.assignment()), "Consumer did not receive partition assignment for " + topic);

    consumer.seekToEnd(consumer.assignment());
    consumer.poll(Duration.ofMillis(100)); // seekToEnd "evaluates lazily, seeking to the final offset in all partitions only when poll(Duration) is called"

    return consumer.position(partition);
  }

  public void pollTopicForEvent(int expectedMessageCount, int timeoutSeconds, Predicate<T> isTestMessage, java.util.function.Consumer<T> validate) {
    pollTopicForEvent(expectedMessageCount, timeoutSeconds, isTestMessage, null, validate);
  }

  public void pollTopicForEvent(int expectedMessageCount, int timeoutSeconds, Predicate<T> isTestMessage, String expectedKey, java.util.function.Consumer<T> validate) {
    LocalDateTime start = LocalDateTime.now();
    int testEventsValidated = 0;

    boolean stillSeeking = true;
    while (stillSeeking && Duration.between(start, LocalDateTime.now()).getSeconds() < timeoutSeconds) {
      ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(Duration.ofSeconds(Math.min(timeoutSeconds, 10)));
      List<T> testMessages = filterTestEventsFromBatch(consumerRecords, isTestMessage, expectedKey);
      if (validate != null)
        testMessages.forEach(validate);

      testEventsValidated += testMessages.size();
      stillSeeking = expectedMessageCount == 0 || testEventsValidated < expectedMessageCount;
    }

    Assertions.assertEquals(expectedMessageCount, testEventsValidated, "Did not receive the expected # of events.");
  }

  private List<T> filterTestEventsFromBatch(ConsumerRecords<String, byte[]> consumerRecords, Predicate<T> isTestMessage, String expectedKey) {
    List<byte[]> protoMessages = new ArrayList<>();
    consumerRecords.forEach(record -> protoMessages.add(record.value()));

    long beginOffset = IterableUtils.isEmpty(consumerRecords) ? 0 : IterableUtils.get(consumerRecords, 0).offset();
    long endOffset = IterableUtils.isEmpty(consumerRecords) ? 0 : IterableUtils.get(consumerRecords, IterableUtils.size(consumerRecords) - 1).offset();
    long byteCount = protoMessages.stream().reduce(0L, (result, message) -> result + message.length, Long::sum);
    log.debug("Consumed {} messages ({} bytes), offets [{}...{}], from topic {}.", protoMessages.size(), byteCount, beginOffset, endOffset, topic);

    List<T> testMessages = protoMessages.stream().map(this::fromProtobuf).filter(isTestMessage).collect(Collectors.toList());

    consumerRecords.forEach(record -> {
      if (testMessages.contains(fromProtobuf(record.value()))) {
        log.info("Test message found! Key: '{}'", record.key());
        log.trace("Found Event: {}", fromProtobuf(record.value()));
        if (null != expectedKey)
          assertEquals(expectedKey, record.key());
      }
    });

    consumer.commitSync(); // TODO: Transition to automatic offset commiting
    return testMessages;
  }

  @SuppressWarnings("unchecked")
  private T fromProtobuf(byte[] message) {
    try {
      return (T) clazz.getDeclaredConstructor().newInstance().fromProtobuf(message);
    } catch (ClassCastException | InvalidProtocolBufferException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
      log.error("Unable to convert message byes '" + Arrays.toString(message) + "'to protobuf", e);
      return null;
    }
  }

  @Override
  public void close() throws IOException {
    consumer.unsubscribe();
    consumer.close();
    log.info("Unsubscribed from topic '{}'.", topic);
  }

}
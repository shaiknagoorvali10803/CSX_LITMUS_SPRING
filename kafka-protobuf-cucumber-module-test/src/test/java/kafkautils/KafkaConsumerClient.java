package kafkautils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Deprecated
public class KafkaConsumerClient {

  private static final Logger log = LoggerFactory.getLogger(KafkaConsumerClient.class);

  private static Integer maxBytes = (Integer) KafkaProperties.getConsumerProperties().get(ConsumerConfig.FETCH_MAX_BYTES_CONFIG);
  private static Integer maxMessages = (Integer) KafkaProperties.getConsumerProperties().get(ConsumerConfig.MAX_POLL_RECORDS_CONFIG);

  private KafkaConsumerClient() {
    throw new IllegalStateException("Utility class");
  }

  private static Consumer<String, byte[]> createKafkaConsumerClient(String topic) {
    final Properties props = KafkaProperties.getConsumerProperties();
    // Create the consumer using props.
    final Consumer<String, byte[]> consumer = new KafkaConsumer<>(props);

    // Subscribe to the topic.
    // https://kafka.apache.org/25/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html
    // See "Controlling The Consumer's Position"
    // interesting discussion -
    // https://stackoverflow.com/questions/55744667/retrieve-last-n-messages-of-kafka-consumer-from-a-particular-topic
    consumer.subscribe(Collections.singletonList(topic));
    log.info("Subscribed to {} partitions on topic {}", consumer.assignment().size(), topic);

    return consumer;
  }

  @Deprecated
  public static byte[] runKafkaConsumer(String topic) {
    final Consumer<String, byte[]> consumerClient = createKafkaConsumerClient(topic);
    ConsumerRecords<String, byte[]> consumerRecords = consumerClient.poll(Duration.ofMinutes(1));
    byte[] valueBytes = null;
    consumerClient.commitAsync();
    consumerClient.close();
    for (ConsumerRecord<String, byte[]> records : consumerRecords) {// should only have 1 value.
      valueBytes = records.value();
      break;
    }
    log.debug("Consumer shutdown complete! {} ", valueBytes);
    return valueBytes;
  }

  @Deprecated
  public static byte[] runKafkaConsumerReadAll(String topic) {
    final Consumer<String, byte[]> consumerClient = createKafkaConsumerClient(topic);
    ConsumerRecords<String, byte[]> consumerRecords = consumerClient.poll(Duration.ofMinutes(1));
    byte[] valueBytes = null;
    for (ConsumerRecord<String, byte[]> records : consumerRecords) {
      valueBytes = records.value();
      log.info("Key {}, partition {}, offset {} ", records.key(), records.partition(), records.offset());
    }
    consumerClient.commitAsync();
    consumerClient.close();
    log.debug("Consumer shutdown complete! {} ", valueBytes);
    return valueBytes;
  }

  public static List<byte[]> runKafkaConsumerGetAllMessages(String topic) {
    final Consumer<String, byte[]> consumerClient = createKafkaConsumerClient(topic);
    ConsumerRecords<String, byte[]> consumerRecords = consumerClient.poll(Duration.ofSeconds(15));
    consumerClient.commitAsync();
    consumerClient.close();
    log.debug("Consumer shutdown complete!");

    List<byte[]> records = new ArrayList<>();
    consumerRecords.forEach((record) -> records.add(record.value()));
    log.info("Consumed {} messages ({} bytes) from topic {}", records.size(), getBytesFromList(records), topic);

    return records;
  }

  public static void consumeAllAvailable(String topic) {
    final Consumer<String, byte[]> consumerClient = createKafkaConsumerClient(topic);

    boolean isTopicSaturated = true;
    while (isTopicSaturated) {
      ConsumerRecords<String, byte[]> consumerRecords = consumerClient.poll(Duration.ofSeconds(10));
      consumerClient.commitSync();

      List<byte[]> records = new ArrayList<>();
      consumerRecords.forEach((record) -> records.add(record.value()));
      long bytesConsumed = getBytesFromList(records);
      log.info("Consumed {} messages ({} bytes) from topic {}", records.size(), bytesConsumed, topic);

      if (consumerRecords.isEmpty() || bytesConsumed < (maxBytes / 3) || records.size() < (maxMessages / 3)) {
        isTopicSaturated = false;
      }
    }

    consumerClient.close();
  }

  private static long getBytesFromList(List list) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream out = new ObjectOutputStream(baos);
      out.writeObject(list);
      out.close();
      return baos.toByteArray().length;

    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return -1;
  }


  public static byte[] runKafkaConsumerReadAllIN20Secs(String topic) {
    final Consumer<String, byte[]> consumerClient = createKafkaConsumerClient(topic);
    ConsumerRecords<String, byte[]> consumerRecords = consumerClient.poll(Duration.ofSeconds(20));
    byte[] valueBytes = null;
    for (ConsumerRecord<String, byte[]> records : consumerRecords) {
      valueBytes = records.value();
      log.info("Key {}, partition {}, offset {} ", records.key(), records.partition(), records.offset());
    }
    consumerClient.commitAsync();
    consumerClient.close();
    log.debug("Consumer shutdown complete! {} ", valueBytes);
    return valueBytes;
  }

}

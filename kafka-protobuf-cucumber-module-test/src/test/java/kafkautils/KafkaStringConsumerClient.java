package kafkautils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaStringConsumerClient {

  private static final Logger log = LoggerFactory.getLogger(KafkaStringConsumerClient.class);

  private KafkaStringConsumerClient() {
    throw new IllegalStateException("Utility class");
  }

  public static String runKafkaConsumerStringOutput(String topic) {
    final Consumer<String, String> consumerClient = createKafkaConsumerClientStringOutput(topic);
    ConsumerRecords<String, String> consumerRecords = consumerClient.poll(Duration.ofMinutes(1));
    String valueBytes = null;
    consumerClient.commitAsync();
    consumerClient.close();
    for (ConsumerRecord<String, String> records : consumerRecords) {// should only have 1 value.
      valueBytes = records.value();
      break;
    }
    log.debug("Consumer shutdown complete! {} ", valueBytes);
    return valueBytes;
  }

  private static Consumer<String, String> createKafkaConsumerClientStringOutput(String topic) {
    final Properties props = KafkaProperties.getPropertiesWithStringDeSerializer();
    // Create the consumer using props.
    final Consumer<String, String> consumer = new KafkaConsumer<>(props);
    // Subscribe to the topic.
    consumer.subscribe(Collections.singletonList(topic));
    return consumer;
  }

  public static List<String> runKafkaConsumerGetAllStringOutput(String topic) {
    final Consumer<String, String> consumerClient = createKafkaConsumerClientStringOutput(topic);
    ConsumerRecords<String, String> consumerRecords = consumerClient.poll(Duration.ofSeconds(10));
    consumerClient.commitAsync();
    consumerClient.close();
    log.debug("Consumer shutdown complete!");

    List<String> records = new ArrayList<>();
    consumerRecords.forEach((record) -> {
      log.info("Key {}, partition {}, offset {} ,value {} ", record.key(), record.partition(), record.offset(), record.value());
      records.add(record.value());
    });

    return records;
  }
}

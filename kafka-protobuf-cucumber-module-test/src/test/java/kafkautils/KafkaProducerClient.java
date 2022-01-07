package kafkautils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.csx.protocol.protobuf.ProtobufMapper;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerClient {

  private static final Properties stringByteProps = KafkaProperties.getProducerProperties();
  private static final Properties stringStringProps = KafkaProperties.getStringProducerProperties();
  private static final Logger log = LoggerFactory.getLogger(KafkaProducerClient.class);

  private KafkaProducerClient() {
    throw new IllegalStateException("Utility class");
  }


  public static boolean send(String inputTopic, String keyValue, ProtobufMapper proto) {
    log.debug("Sending {}", proto);
    return sendProtobuffMessageToTopicWithStringKey(inputTopic, keyValue, proto.toProtobuf());
  }


  public static boolean sendProtobuffMessageToTopicWithStringKey(String inputTopic, String keyValue, byte[] protoBytes) {
    if (null != inputTopic && null != protoBytes) {
      Producer<String, byte[]> producer = new KafkaProducer<>(stringByteProps);
      Future<RecordMetadata> t = producer.send(new ProducerRecord<>(inputTopic, keyValue, protoBytes));
      assertNotNull(t);
      try {
        log.debug("RecordMetadata produced. Offset: {}, Timestamp: {} ", t.get().offset(), t.get().timestamp());
      } catch (InterruptedException | ExecutionException e) {
        log.error(e.getMessage(), e);
        producer.close();
        return false;
      }

      log.info("Message sent successfully to '{}' with key '{}'", inputTopic, keyValue);
      producer.close();
      return true;
    }
    log.warn("Invalid KafkaProducerClient parameters - inputTopic or protoBytes are null");
    return false;
  }

  public static boolean sendStringMessageToTopicWithStringKey(String inputTopic, String keyValue, String messageStr) {
    if (null != inputTopic && null != messageStr) {
      Producer<String, String> producer = new KafkaProducer<>(stringStringProps);
      Future<RecordMetadata> t = producer.send(new ProducerRecord<>(inputTopic, keyValue, messageStr));
      assertNotNull(t);
      try {
        // t.get() will throw an exception if the Producer.send() was not successful
        log.debug(t.get().toString());
      } catch (InterruptedException | ExecutionException e) {
        log.error(e.getMessage(), e);
        producer.close();
        return false;
      }
      log.debug("message sent successfully to {} : ", inputTopic);
      producer.close();
      return true;
    }
    log.debug("inputTopic or messageStr are null");
    return false;
  }

}

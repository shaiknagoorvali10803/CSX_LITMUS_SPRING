package kafkautils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaProperties {

  private static final String KEY_STORE_LOCATION_VALUE = "/keystore/keystore.jks";
  private static final String TRUST_STORE_LOCATION_VALUE = "/keystore/truststore.jks";

  private static final String KEY_TRUST_STORE_PASSWORD = System.getProperty("secret");
  private static final String EXECUTE_LOCAL_ARG = System.getProperty("executeLocal");

  private static final String OFFSET = "latest";
  private static final String STRING_DESERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";
  private static final String GROUP_ID = "KafkaConsumerClient-Test";

  private KafkaProperties() {}

  static {
    if (!StringUtils.equals("true", EXECUTE_LOCAL_ARG)) {
      assertNotNull(KEY_TRUST_STORE_PASSWORD, "command line argument 'secret' not provided.");
    }
  }

  private static Properties getCommonProperties() {
    Properties props = new Properties();

    if (StringUtils.equals("true", EXECUTE_LOCAL_ARG)) {
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    } else {
      props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "events-cluster-kafka-bootstrap-events-kafka-dev.go-dev.csx.com:443");

      // see http://kafka.apache.org/090/documentation.html#producerconfigs
      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
      props.put("ssl.keystore.location", KEY_STORE_LOCATION_VALUE);
      props.put("ssl.keystore.password", KEY_TRUST_STORE_PASSWORD);
      props.put("ssl.truststore.location", TRUST_STORE_LOCATION_VALUE);
      props.put("ssl.truststore.password", KEY_TRUST_STORE_PASSWORD);
    }

    return props;
  }

  public static Properties getProducerProperties() {
    Properties props = getCommonProperties();
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");

    return props;
  }

  public static Properties getStringProducerProperties() {
    Properties props = getCommonProperties();
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

    return props;
  }

  public static Properties getConsumerProperties() {
    Properties props = getCommonProperties();
    props.put(CommonClientConfigs.GROUP_ID_CONFIG, GROUP_ID);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");

    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET);
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500); // 500 default
    props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1048576); // 1048576 default

    return props;
  }

  public static Properties getPropertiesWithStringDeSerializer() {
    Properties props = getCommonProperties();
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, STRING_DESERIALIZER_VALUE);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, STRING_DESERIALIZER_VALUE);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET);
    return props;
  }

}

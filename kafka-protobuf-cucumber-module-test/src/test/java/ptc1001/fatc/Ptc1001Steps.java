package ptc1001.fatc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.railroad.protocols.emp.models.Emp1001;
import cucumber.api.java8.En;
import java.util.function.Predicate;
import kafkautils.KafkaProducerClient;
import kafkautils.KafkaStreamingClient;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ptc1001Steps implements En {

  private final Logger log = LoggerFactory.getLogger(Ptc1001Steps.class);

  private String inputTopic = "emp.1001";
  private String outputTopic = "com.csx.asset.rolling.loco.ptc1001";
  KafkaStreamingClient<Emp1001> kafkaStreamingClient;

  public Ptc1001Steps() {

    Before(() -> {
      kafkaStreamingClient = new KafkaStreamingClient<>(outputTopic, Emp1001.class);
    });

    After(() -> {
      kafkaStreamingClient.close();
    });

    Given("a emp1001 message", () -> {
      String emp_message =
          "0403E9021100003D000023135F73854B24003B0028637378742E623A63623100637378742E6C2E637378742E323638363A6974630043535854011859323336323920202020494E4448415730393239313830301E5932333632392020202020494E4448415720494E444841572031343A3330971638EB";
      byte[] empMessageBytes = Hex.decodeHex(emp_message.toCharArray());
      assertTrue(KafkaProducerClient.sendProtobuffMessageToTopicWithStringKey(inputTopic, "somekey", empMessageBytes));
    });

    Then("validate results", () -> {

      Predicate<Emp1001> isTestMessage = emp1001 -> StringUtils.equals("csxt2686", emp1001.getLocoId());

      java.util.function.Consumer<Emp1001> testValidate = emp1001 -> {
        assertNotNull(emp1001);
        assertNotNull(emp1001.getHeader());
        assertNotNull(emp1001.getHeader().getTimestamp());

        assertEquals("csxt.b:cb1", emp1001.getHeader().getSourceAddress());
        assertEquals("csxt2686", emp1001.getLocoId());
        assertEquals("CSXT", emp1001.getScac());

        assertNotNull(emp1001.getTrainIds().get(0));
        assertEquals("Y23629    INDHAW09291800", emp1001.getTrainIds().get(0).getTrainId());
        assertEquals("Y23629     INDHAW INDHAW 14:30", emp1001.getTrainIds().get(0).getDisplayTrainId());
      };

      kafkaStreamingClient.pollTopicForEvent(1, 90, isTestMessage, testValidate);
    });
  }

}

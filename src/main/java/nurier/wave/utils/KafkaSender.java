

package nurier.wave.utils;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaSender {
    /** 
     * config
     */
    public static String BROKER_URL = "192.168.0.42:9092,192.168.0.43:9092,192.168.0.46:9092";
    public static String GROUP_ID = "admin_rule_creating";
    public static String TOPIC = "rule_pattern";
    
    public static KafkaProducer<String, String> producer = null;
    
    public void kafkaProducerSender(String message) {
        try {
            submitKafkaProducer(TOPIC, message);
            Thread.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static void submitKafkaProducer(String topic, String value) {
        try {
            if ( producer == null ) {
                Properties props = new Properties();
                props.put("bootstrap.servers", BROKER_URL);
                props.put("key.serializer", StringSerializer.class.getName());
                props.put("value.serializer", StringSerializer.class.getName());
        
                producer = new KafkaProducer(props);
            }
            
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

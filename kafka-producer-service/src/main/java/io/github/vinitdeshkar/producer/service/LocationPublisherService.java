
package io.github.vinitdeshkar.producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vinitdeshkar.producer.model.DriverLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Service
public class LocationPublisherService {

    private static final Logger logger = LoggerFactory.getLogger(LocationPublisherService.class);

    @Value("${kafka.topic.driver-location}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public void publishLocation(DriverLocation location) {
        try {
            String key = location.getCountryCode() + "-" + location.getDriverId();
            logger.info(key);
            String value = mapper.writeValueAsString(location);
            sendMessage(topic, key, value);
        } catch (Exception e) {
            logger.error("Error occurred while publishing driver location", e);
        }
    }

    private void sendMessage(String topic, String key, String value) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, value);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                logger.info(
                        "\n--------------------------------------\n" +
                                "🚗 Driver location published successfully ✅\n" +
                                "Topic     : {}\n" +
                                "Partition : {}\n" +
                                "Offset    : {}\n" +
                                "Timestamp : {}\n" +
                                "--------------------------------------",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()
                );
            } else {
                logger.error("❌ Failed to produce record to Kafka", ex);
            }
        });
    }
}

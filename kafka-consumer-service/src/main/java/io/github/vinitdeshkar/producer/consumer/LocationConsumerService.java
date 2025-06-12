
package io.github.vinitdeshkar.producer.consumer;

import io.github.vinitdeshkar.producer.model.DriverLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LocationConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(LocationConsumerService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.driver-location}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String key = record.key();
            String value = record.value();

            DriverLocation location = objectMapper.readValue(value, DriverLocation.class);

            System.out.println("---------------------------------------------------------------------------------");
            System.out.println("📡 Location consumer received an update for driver: " + location.getDriverId());
            System.out.println("🌍 Country Code: " + location.getCountryCode());
            System.out.println("📍 Coordinates: " + location.getLatitude() + ", " + location.getLongitude());
            System.out.println("🕒 Time: " + location.getTimestamp());
            notifyRider(location);
            notifySharedUsers(location);
            System.out.println("---------------------------------------------------------------------------------");

        } catch (Exception e) {
            logger.error("Error occurred while consuming driver location: ", e);
        }
    }

    private void notifyRider(DriverLocation location) {
        System.out.println("📨 Rider notified: Driver " + location.getDriverId() + " location updated.");
    }

    private void notifySharedUsers(DriverLocation location) {
        System.out.println("📨 Shared users notified: Driver " + location.getDriverId() + " location updated.");
    }
}

package io.github.vinitdeshkar.consumer.consumer;

import io.github.vinitdeshkar.consumer.model.DriverLocation;
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

            logger.info(
                    """
                    ---------------------------------------------------------------------------------
                    📡 Location consumer received an update for driver: {}
                    🌍 Country Code: {}
                    📍 Coordinates: {}, {}
                    🕒 Time       : {}
                    📨 Rider notified: Driver {} location updated.
                    📨 Shared users notified: Driver {} location updated.
                    ---------------------------------------------------------------------------------
                    """,
                    location.getDriverId(),
                    location.getCountryCode(),
                    String.format("%.4f", location.getLatitude()),
                    String.format("%.4f", location.getLongitude()),
                    location.getTimestamp(),
                    location.getDriverId(),
                    location.getDriverId()
            );

            notifyRider(location);
            notifySharedUsers(location);

        } catch (Exception e) {
            logger.error("Error occurred while consuming driver location: ", e);
        }
    }

    private void notifyRider(DriverLocation location) {
        // System.out.println("📨 Rider notified: Driver " + location.getDriverId() + " location updated.");
    }

    private void notifySharedUsers(DriverLocation location) {
        // System.out.println("📨 Shared users notified: Driver " + location.getDriverId() + " location updated.");
    }
}

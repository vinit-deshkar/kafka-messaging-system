package io.github.vinitdeshkar.consumer.consumer;

import io.github.vinitdeshkar.consumer.model.DriverLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumerService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic.driver-location}", groupId = "${notification.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String key = record.key();
            String value = record.value();

            DriverLocation location = objectMapper.readValue(value, DriverLocation.class);

            logger.info(
                    """
                    ---------------------------------------------------------------------------------
                    📢 Notification consumer received an update for driver: {}
                    📍 Coordinates: {}, {}
                    🕒 Time       : {}
                    📱 Sending push notifications for driver: {}
                    ---------------------------------------------------------------------------------
                    """,
                    location.getDriverId(),
                    String.format("%.4f", location.getLatitude()),
                    String.format("%.4f", location.getLongitude()),
                    location.getTimestamp(),
                    location.getDriverId()
            );
            sendNotifications(location);

        } catch (Exception e) {
            logger.error("Error occurred while consuming driver location: ", e);
        }
    }

    private void sendNotifications(DriverLocation location) {
        //  System.out.println("📱 Sending push notifications to rider: " + location.getDriverId());
    }
} 
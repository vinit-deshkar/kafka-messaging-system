package io.github.vinitdeshkar.producer.consumer;

import io.github.vinitdeshkar.producer.model.DriverLocation;
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

            System.out.println("---------------------------------------------------------------------------------");
            System.out.println("📢 Notification consumer received an update for driver " + location.getDriverId());
            System.out.println("📍 Coordinates: " + location.getLatitude() + ", " + location.getLongitude());
            System.out.println("🕒 Time: " + location.getTimestamp());
            sendNotifications(location);
            System.out.println("---------------------------------------------------------------------------------");

        } catch (Exception e) {
            logger.error("Error occurred while consuming driver location: ", e);
        }
    }

    private void sendNotifications(DriverLocation location) {
        System.out.println("📱 Sending push notifications for driver: " + location.getDriverId());
    }
} 
package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }
    static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    public void send(String message) {

        // Clean the message to remove any unwanted characters (such as newlines, spaces, etc.)
        String cleanedMessage = message.replaceAll("[^0-9.]", "");

        try {
            // Now, parse the cleaned message as a float
            float amount = Float.parseFloat(cleanedMessage);
            // Proceed with your logic after parsing the float
        } catch (NumberFormatException e) {
            // Log an error if parsing fails
            logger.error("Invalid number format for message: " + message, e);
        }
    }
}
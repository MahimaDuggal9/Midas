package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {

    private static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void taskThreeVerifier() throws InterruptedException {
        // Populate user data (Make sure to handle potential errors in userPopulator)
        try {
            userPopulator.populate();
        } catch (Exception e) {
            logger.error("Error populating user data", e);
            return; // Exit the test if populating user data fails
        }

        // Load transaction lines from the file
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");

        // Send each transaction line to Kafka
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Sleep to allow processing of the messages
        Thread.sleep(2000);

        // Log instructions and ask for debugging
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("Use your debugger to find out what Waldorf's balance is after all transactions are processed.");
        logger.info("Kill this test once you find the answer.");

        // Infinite loop to keep the test running until manually stopped
        while (true) {
            Thread.sleep(20000); // Sleep for 20 seconds
            logger.info("Waiting for the result...");
        }
    }
}

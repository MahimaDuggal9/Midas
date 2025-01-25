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
class TaskTwoTests {
    static final Logger logger = LoggerFactory.getLogger(TaskTwoTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_two_verifier() throws InterruptedException {
        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");

        for (String transactionLine : transactionLines) {
            String sanitizedTransaction = sanitizeTransactionLine(transactionLine);
            if (sanitizedTransaction != null) {
                kafkaProducer.send(sanitizedTransaction);
            } else {
                logger.warn("Skipping invalid transaction: " + transactionLine);
            }
        }

        Thread.sleep(2000);

        logger.info("----------------------------------------------------------");
        logger.info("use your debugger to watch for incoming transactions");
        logger.info("kill this test once you find the answer");
        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }

    /**
     * Sanitizes a transaction line by trimming whitespace and validating format.
     * @param transactionLine The raw transaction string.
     * @return The sanitized transaction line, or null if invalid.
     */
    private String sanitizeTransactionLine(String transactionLine) {
        if (transactionLine == null) {
            return null;
        }

        String sanitizedLine = transactionLine.trim();
        if (!sanitizedLine.matches("\\d+(\\.\\d+)?")) { // Validates numeric format
            logger.error("Invalid transaction format: " + sanitizedLine);
            return null;
        }

        return sanitizedLine;
    }
}

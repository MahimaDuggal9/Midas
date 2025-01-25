package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator {
    @Autowired
    private FileLoader fileLoader;

    private static final Logger logger = LoggerFactory.getLogger(UserPopulator.class);
    @Autowired
    private DatabaseConduit databaseConduit;

    public void populate() {
        String[] userLines = fileLoader.loadStrings("/test_data/lkjhgfdsa.hjkl");
        for (String userLine : userLines) {
            String[] userData = userLine.split(", ");

            // Clean the second element to keep only valid numeric values (including decimal points)
            String cleanedAmount = userData[1].replaceAll("[^0-9.]", "");

            try {
                // Parse the cleaned string as a float
                float amount = Float.parseFloat(cleanedAmount);
                UserRecord user = new UserRecord(userData[0], amount);
                databaseConduit.save(user);
            } catch (NumberFormatException e) {
                // Log the invalid value
                logger.error("Invalid amount format for user: " + userData[0] + ", amount: " + userData[1], e);
            }
        }
    }

}

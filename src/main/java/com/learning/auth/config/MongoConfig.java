package com.learning.auth.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${database.name:auth-server}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return this.databaseName;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        String username = System.getenv("MONGO_USERNAME");
        String password = System.getenv("MONGO_PASSWORD");
        String host = System.getenv("MONGO_HOST");
        // Validate that the environment variables are set
        if (StringUtils.isAnyBlank(username, password, host)) {
            LOGGER.error("MongoDB connection details are not set in environment variables.");
            throw new IllegalStateException("MongoDB connection details are not set.");
        }
        String connectionString = String.format("mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority", username, password, host, this.databaseName);
        LOGGER.debug("MongoDB connection string: {}", connectionString);
        return MongoClients.create(connectionString);
    }
}

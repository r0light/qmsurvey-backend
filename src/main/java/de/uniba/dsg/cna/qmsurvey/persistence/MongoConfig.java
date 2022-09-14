package de.uniba.dsg.cna.qmsurvey.persistence;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Collection;
import java.util.Collections;

@Profile("prod")
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${qmsurvey.data.mongodb.host}")
    private String host;

    @Value("${qmsurvey.data.mongodb.port}")
    private int port;

    @Value("${qmsurvey.data.mongodb.database}")
    private String database;

    @Value("${qmsurvey.data.mongodb.username}")
    private String username;

    @Value("${qmsurvey.data.mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return "qmsurvey";
    }

    @Override
    public MongoClient mongoClient() {
        String prodConnectionString = String.format("mongodb://%s:%s@%s:%d", username, password, host, port);
        ConnectionString connectionString = new ConnectionString(prodConnectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("de.uniba.dsg.cna.qmsurvey.persistence");
    }
}
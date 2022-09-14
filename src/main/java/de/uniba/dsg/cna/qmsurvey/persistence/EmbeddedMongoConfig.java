package de.uniba.dsg.cna.qmsurvey.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PreDestroy;

@Profile("dev")
@Import(EmbeddedMongoAutoConfiguration.class)
@Configuration
public class EmbeddedMongoConfig {

    Logger logger = LoggerFactory.getLogger(EmbeddedMongoConfig.class);

    private static final String CONNECTION_STRING = "mongodb://%s:%d";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;


    private MongodExecutable mongodExecutable;
    private MongodProcess mongod;
    private MongoClient client;

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        ImmutableMongodConfig mongoDbConfig = MongodConfig.builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();

        mongodExecutable = starter.prepare(mongoDbConfig);
        mongod = mongodExecutable.start();
        client = MongoClients.create(String.format(CONNECTION_STRING, HOST, PORT));

        return new MongoTemplate( client, "qmsurvey");
    }


    /*
     * solution found here: https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo/issues/212
     */
    @PreDestroy
    public void shutdownGracefully() {
        if (client != null)
            client.close();
        if (mongod != null)
            try {
                mongod.stop();
            } catch (IllegalStateException e) {
                logger.error("Can't stop mongod", e);
            }
        if (mongod != null) {
            int limit = 5;
            int counter = 0;
            while (mongod.isProcessRunning() && counter < limit) {
                try {
                    counter++;
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        if (mongodExecutable != null) {
            try {
                mongodExecutable.stop();
            } catch (IllegalStateException e) {
                logger.error("Can't stop mongodExecutable", e);
            }
        }
    }
}
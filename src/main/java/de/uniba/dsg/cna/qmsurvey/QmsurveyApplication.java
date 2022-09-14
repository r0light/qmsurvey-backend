package de.uniba.dsg.cna.qmsurvey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class QmsurveyApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(QmsurveyApplication.class, args);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (!"stop".equals(in.readLine())) { }
		} catch (IOException e) {
			System.err.println("Input read error, exiting");
		}
		context.close();
	}
}
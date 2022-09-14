package de.uniba.dsg.cna.qmsurvey.webapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "QM Validation Survey",
                description = "" +
                        "API for quality model validation surveys",
                contact = @Contact(
                        name = "DSG",
                        url = "https://www.uni-bamberg.de/pi/"
                )/*,
                license = @License(
                        name = "MIT Licence",
                        url = "https://github.com/...")*/
        ),
        servers = {@Server(url = "http://localhost:8080/backend"), @Server(url = "https://qmsurvey.pi.uni-bamberg.de/backend")}
)
public class OpenApiConfiguration {
}

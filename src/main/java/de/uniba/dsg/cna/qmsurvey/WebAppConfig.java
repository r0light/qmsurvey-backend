package de.uniba.dsg.cna.qmsurvey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Value("${qmsurvey.cors.client}")
    private String clientHost;

    @Controller
    static class FaviconController {
        @RequestMapping("favicon.ico")
        String favicon() {
            return "forward:/images/favicon.ico";
        }
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(false);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration registration = registry.addMapping("/**");
        registration.allowedOrigins(clientHost);
    }

}

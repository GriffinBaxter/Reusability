package org.seng302.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"org.seng302"})
@EntityScan("org.seng302")
@EnableJpaRepositories("org.seng302")
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("-- booting up application --");
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
    }

    /**
     * This method specifies the allowed origins (localhost and your VM), HTTP
     * methods and url mappings (all, denoted by "/**")
     *
     * @see https://spring.io/guides/gs/rest-service-cors/
     *
     * @return the configurer that check incoming origins for CORS purposes
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedOrigins("http://localhost:9500", "https://csse-s302g12.canterbury.ac.nz");
            }
        };
    }
}

package com.itic.intranet;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IticIntranetApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        System.setProperty("SPRING_APPLICATION_NAME", dotenv.get("SPRING_APPLICATION_NAME"));
        System.setProperty("SPRING_DATASOURCE_URL_LOCAL", dotenv.get("SPRING_DATASOURCE_URL_LOCAL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATA_MONGODB_URI", dotenv.get("SPRING_DATA_MONGODB_URI"));
        System.setProperty("SPRING_DATA_MONGODB_DATABASE", dotenv.get("SPRING_DATA_MONGODB_DATABASE"));
        System.setProperty("JWT_TOKEN", dotenv.get("JWT_TOKEN"));

        SpringApplication.run(IticIntranetApplication.class, args);
    }
}

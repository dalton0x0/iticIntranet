package com.itic.intranet;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IticIntranetApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        System.setProperty("MYSQL_CONNECTION", dotenv.get("MYSQL_CONNECTION"));
        System.setProperty("MYSQL_USERNAME", dotenv.get("MYSQL_USERNAME"));
        System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
        System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
        System.setProperty("MONGODB_DATABASE", dotenv.get("MONGODB_DATABASE"));

        SpringApplication.run(IticIntranetApplication.class, args);
    }
}

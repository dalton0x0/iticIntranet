package com.itic.intranet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IticIntranetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IticIntranetApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {};
    }
}
package com.skillshare.photography;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mongodb.client.MongoDatabase;

@SpringBootApplication
public class PhotographyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotographyApplication.class, args);
        System.out.println("📸 Photography Application started successfully!");
    }

    @Bean
    CommandLineRunner init(MongoDatabase mongoDatabase) {
        return args -> {
            System.out.println("✅ Connected to MongoDB database: " + mongoDatabase.getName());
        };
    }
}

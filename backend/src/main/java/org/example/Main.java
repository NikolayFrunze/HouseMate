package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot backend.
 * @SpringBootApplication enables auto-configuration and scans this package
 * (and sub-packages) for controllers, services, repositories and configs.
 */
@SpringBootApplication
public class Main {
    // Starts the application and the embedded web server (Tomcat, port 8080 by default)
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

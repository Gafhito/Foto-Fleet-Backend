package com.digitalhouse.fotofleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FotofleetApplication {
    public static void main(String[] args) {
        SpringApplication.run(FotofleetApplication.class, args);

    }
}

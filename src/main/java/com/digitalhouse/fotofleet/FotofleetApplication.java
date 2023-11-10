package com.digitalhouse.fotofleet;

import com.digitalhouse.fotofleet.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class FotofleetApplication {


    public static void main(String[] args) {
        SpringApplication.run(FotofleetApplication.class, args);

    }




}





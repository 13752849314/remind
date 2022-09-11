package com.example.remind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class RemindApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemindApplication.class, args);
    }

}

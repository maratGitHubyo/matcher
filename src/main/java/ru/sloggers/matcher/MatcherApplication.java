package ru.sloggers.matcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatcherApplication.class, args);
    }

}

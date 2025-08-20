package com.zhh.handsome.tileapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class TileAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TileAppApplication.class, args);
    }
}
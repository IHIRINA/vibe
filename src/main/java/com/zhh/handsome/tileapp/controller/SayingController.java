package com.zhh.handsome.tileapp.controller;

import com.zhh.handsome.tileapp.service.SayingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saying")
@Slf4j
public class SayingController {
    @Autowired
    private SayingService sayingService;
    @GetMapping
    public ResponseEntity<?> sayHello() {
        return sayingService.getSayings();
    }
}

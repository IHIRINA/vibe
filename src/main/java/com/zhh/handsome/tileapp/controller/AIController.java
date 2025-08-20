package com.zhh.handsome.tileapp.controller;

import com.zhh.handsome.tileapp.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping(value = "/chat",produces = "text/html;charset=UTF-8")
    public Flux<String> chat(@RequestParam String content) {
       return aiService.chat(content);
    }
}
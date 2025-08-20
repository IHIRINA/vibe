package com.zhh.handsome.tileapp.service;

import reactor.core.publisher.Flux;

public interface AIService {
    Flux<String> chat(String content);
}
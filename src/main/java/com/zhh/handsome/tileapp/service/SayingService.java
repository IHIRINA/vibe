package com.zhh.handsome.tileapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface SayingService {
    ResponseEntity<?> getSayings();
}

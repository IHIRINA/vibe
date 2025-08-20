package com.zhh.handsome.tileapp.service.impl;

import com.zhh.handsome.tileapp.model.Saying;
import com.zhh.handsome.tileapp.repository.SayingRepository;
import com.zhh.handsome.tileapp.service.SayingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class SayingServiceimpl implements SayingService {
    @Autowired
    private SayingRepository sayingRepository;
    @Override
    public ResponseEntity<?> getSayings() {
        //从数据库中获取任意一条saying
        List<Saying> all = sayingRepository.findAll();
        if(all.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "code", 404,
                    "msg", "No sayings found"
            ));
        }
        Random random = new Random();
        int index = random.nextInt(10);
        Saying saying = all.get(index);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "Saying retrieved successfully");
        response.put("data", saying);
        return ResponseEntity.ok(response);
    }
}

package com.zhh.handsome.tileapp.service.impl;

import com.zhh.handsome.tileapp.model.Saying;
import com.zhh.handsome.tileapp.repository.SayingRepository;
import com.zhh.handsome.tileapp.service.SayingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SayingServiceimpl implements SayingService {
    @Autowired
    private SayingRepository sayingRepository;
    @Override
    public ResponseEntity<?> getSayings() {
        //从数据库中获取任意一条saying
        List<Saying> all = sayingRepository.findAll();
        Random random = new Random();
        int index = random.nextInt(all.size());
        Saying saying = all.get(index);
        return ResponseEntity.ok(saying);
    }
}

package com.zhh.handsome.tileapp.repository;


import com.zhh.handsome.tileapp.model.Saying;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SayingRepository extends JpaRepository<Saying, Long> {
}
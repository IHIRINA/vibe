package com.zhh.handsome.tileapp.repository;


import com.zhh.handsome.tileapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByUserId(Long userId);
}
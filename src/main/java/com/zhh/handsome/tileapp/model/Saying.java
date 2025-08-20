package com.zhh.handsome.tileapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sayings")
@Data
public class Saying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;
}
package com.zhh.handsome.tileapp.repository;


import com.zhh.handsome.tileapp.model.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TileRepository extends JpaRepository<Tile, Long> {
    List<Tile> findByUserId(Long userId);
}
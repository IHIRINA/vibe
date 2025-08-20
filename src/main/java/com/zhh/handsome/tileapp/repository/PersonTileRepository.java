package com.zhh.handsome.tileapp.repository;


import com.zhh.handsome.tileapp.model.PersonTile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonTileRepository extends JpaRepository<PersonTile, Long> {
    List<PersonTile> findByPersonId(Long personId);
    List<PersonTile> findByTileId(Long tileId);
    void deleteByPersonIdAndTileId(Long personId, Long tileId);
}
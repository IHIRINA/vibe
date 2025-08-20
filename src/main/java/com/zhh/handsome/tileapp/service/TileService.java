package com.zhh.handsome.tileapp.service;

import com.zhh.handsome.tileapp.model.Tile;

import java.util.List;

public interface TileService {
    Tile createTile(Long userId, String content);
    Tile updateTile(Long tileId, Long userId, String content);
    void deleteTile(Long tileId, Long userId);
    List<Tile> getTilesByUser(Long userId);
    Tile getTileById(Long tileId);
}
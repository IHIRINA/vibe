package com.zhh.handsome.tileapp.service.impl;


import com.zhh.handsome.tileapp.model.Tile;
import com.zhh.handsome.tileapp.model.User;
import com.zhh.handsome.tileapp.repository.TileRepository;
import com.zhh.handsome.tileapp.repository.UserRepository;
import com.zhh.handsome.tileapp.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TileServiceImpl implements TileService {

    @Autowired
    private TileRepository tileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Tile createTile(Long userId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tile tile = new Tile();
        tile.setUser(user);
        tile.setContent(content);
        tile.setCreatedAt(LocalDateTime.now());
        tile.setUpdatedAt(LocalDateTime.now());

        return tileRepository.save(tile);
    }

    @Override
    public Tile updateTile(Long tileId, Long userId, String content) {
        Tile tile = tileRepository.findById(tileId)
                .orElseThrow(() -> new RuntimeException("Tile not found"));

        if (!tile.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this tile");
        }

        tile.setContent(content);
        tile.setUpdatedAt(LocalDateTime.now());

        return tileRepository.save(tile);
    }

    @Override
    public void deleteTile(Long tileId, Long userId) {
        Tile tile = tileRepository.findById(tileId)
                .orElseThrow(() -> new RuntimeException("Tile not found"));

        if (!tile.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this tile");
        }

        tileRepository.delete(tile);
    }

    @Override
    public List<Tile> getTilesByUser(Long userId) {
        return tileRepository.findByUserId(userId);
    }

    @Override
    public Tile getTileById(Long tileId) {
        return tileRepository.findById(tileId)
                .orElseThrow(() -> new RuntimeException("Tile not found"));
    }
}
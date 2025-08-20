package com.zhh.handsome.tileapp.controller;


import com.zhh.handsome.tileapp.model.Tile;
import com.zhh.handsome.tileapp.service.TileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tiles")
public class TileController {

    @Autowired
    private TileService tileService;

    @PostMapping
    public ResponseEntity<?> createTile(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String content = request.get("content").toString();

            Tile tile = tileService.createTile(userId, content);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Tile created successfully");
            response.put("data", Map.of(
                    "id", tile.getId(),
                    "content", tile.getContent(),
                    "createdAt", tile.getCreatedAt(),
                    "updatedAt", tile.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @PutMapping("/{tileId}")
    public ResponseEntity<?> updateTile(@PathVariable Long tileId, @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String content = request.get("content").toString();

            Tile tile = tileService.updateTile(tileId, userId, content);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Tile updated successfully");
            response.put("data", Map.of(
                    "id", tile.getId(),
                    "content", tile.getContent(),
                    "updatedAt", tile.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{tileId}")
    public ResponseEntity<?> deleteTile(@PathVariable Long tileId, @RequestParam Long userId) {
        try {
            tileService.deleteTile(tileId, userId);

            return ResponseEntity.ok(Map.of(
                    "code", 200,
                    "msg", "Tile deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getTilesByUser(@RequestParam Long userId) {
        try {
            List<Tile> tiles = tileService.getTilesByUser(userId);
            List<Map<String, ? extends Serializable>> tilesData = new ArrayList<>();
            for (Tile tile : tiles) {
                Map<String, ? extends Serializable> id = Map.of(
                        "id", tile.getId(),
                        "content", tile.getContent(),
                        "createdAt", tile.getCreatedAt(),
                        "updatedAt", tile.getUpdatedAt()
                );
                tilesData.add(id);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Tiles retrieved successfully");
            response.put("data", tilesData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }

    @GetMapping("/{tileId}")
    public ResponseEntity<?> getTileById(@PathVariable Long tileId) {
        try {
            Tile tile = tileService.getTileById(tileId);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Tile retrieved successfully");
            response.put("data", Map.of(
                    "id", tile.getId(),
                    "content", tile.getContent(),
                    "createdAt", tile.getCreatedAt(),
                    "updatedAt", tile.getUpdatedAt()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "code", 400,
                    "msg", e.getMessage()
            ));
        }
    }
}
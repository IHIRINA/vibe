package com.zhh.handsome.tileapp.controller;

import com.zhh.handsome.tileapp.model.User;
import com.zhh.handsome.tileapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String nickname = request.get("nickname");

            if (username == null || password == null || nickname == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "code", 400,
                        "msg", "Missing required fields"
                ));
            }

            User user = userService.registerUser(username, password, nickname);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "User registered successfully");
            response.put("data", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "nickname", user.getNickname()
            ));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "code", 409,
                    "msg", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "msg", "Missing username or password"
            ));
        }

        boolean isAuthenticated = userService.authenticate(username, password);
        if (isAuthenticated) {
            Optional<User> userOpt = userService.findByUsername(username);
            User user = userOpt.get();
            // 这里应该生成JWT令牌，为简化示例，我们直接返回用户信息
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Login successful");
            response.put("data", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "nickname", user.getNickname(),
                    "avatar", user.getAvatar()
            ));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "code", 401,
                    "msg", "Invalid username or password"
            ));
        }
    }
}
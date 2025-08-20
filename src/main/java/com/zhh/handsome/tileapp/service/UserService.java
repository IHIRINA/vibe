package com.zhh.handsome.tileapp.service;

import com.zhh.handsome.tileapp.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(String username, String password, String nickname);
    Optional<User> findByUsername(String username);
    boolean authenticate(String username, String password);
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
    User updateUserProfile(Long userId, String nickname, String avatar);
}
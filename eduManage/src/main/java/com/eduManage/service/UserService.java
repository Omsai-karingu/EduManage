package com.eduManage.service;

import com.eduManage.entity.User;

public interface UserService {

    // ========= USER MANAGEMENT =========
    User register(User user);

    User getUserById(int id);

    User updateUser(int id, User user);

    void delete(int id);

    // ========= ROLE MANAGEMENT =========
    User updateUserRole(int id, String role);

    // ========= FORGOT PASSWORD / OTP =========
    String generateOtp(String email);

    String verifyOtp(String email, String otp);

    String resetPassword(String email, String newPassword);
}

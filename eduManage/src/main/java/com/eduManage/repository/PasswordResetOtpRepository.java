package com.eduManage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduManage.entity.PasswordResetOtp;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Integer> {
	Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);

    void deleteByEmail(String email);
}

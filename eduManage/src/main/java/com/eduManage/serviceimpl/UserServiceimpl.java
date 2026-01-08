package com.eduManage.serviceimpl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eduManage.entity.Instructor;
import com.eduManage.entity.PasswordResetOtp;
import com.eduManage.entity.User;
import com.eduManage.entity.User.Role;
import com.eduManage.repository.InstructorRepo;
import com.eduManage.repository.PasswordResetOtpRepository;
import com.eduManage.repository.UserRepository;
import com.eduManage.service.EmailService;
import com.eduManage.service.UserService;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private InstructorRepo instructorRepo;

    @Autowired
    private PasswordResetOtpRepository otpRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= REGISTER =================
    @Override
    public User register(User user) {

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User is Already Present");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.STUDENT);
        }

        userRepo.save(user);

        // ✅ Auto-create instructor if role = INSTRUCTOR
        if (user.getRole() == Role.INSTRUCTOR) {
            createInstructorIfNotExists(user);
        }

        return user;
    }

    // ================= GET USER =================
    @Override
    public User getUserById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // ================= UPDATE USER =================
    @Override
    public User updateUser(int id, User user) {

        User existing = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());

        return userRepo.save(existing);
    }

    // ================= UPDATE ROLE =================
    @Override
    public User updateUserRole(int id, String role) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role newRole = Role.valueOf(role);
        user.setRole(newRole);
        userRepo.save(user);

        // ✅ CRITICAL FIX — instructor auto creation
        if (newRole == Role.INSTRUCTOR) {
            createInstructorIfNotExists(user);
        }

        return user;
    }

    // ================= DELETE USER =================
    @Override
    public void delete(int id) {
        userRepo.deleteById(id);
    }

    // ================= OTP GENERATION =================
    @Override
    public String generateOtp(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setEmail(email);
        resetOtp.setOtp(otp);
        resetOtp.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        otpRepo.deleteByEmail(email);
        otpRepo.save(resetOtp);

        emailService.sendOtp(email, otp);

        return "OTP sent to email";
    }

    // ================= OTP VERIFY =================
    @Override
    public String verifyOtp(String email, String otp) {

        PasswordResetOtp resetOtp = otpRepo.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (resetOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            return "OTP expired";
        }

        return "OTP verified";
    }

    // ================= RESET PASSWORD =================
    @Override
    public String resetPassword(String email, String newPassword) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        otpRepo.deleteByEmail(email);

        return "Password reset successfully";
    }

    // ================= PRIVATE HELPER =================
    private void createInstructorIfNotExists(User user) {

        if (!instructorRepo.findByUser_Id(user.getId()).isPresent()) {
            Instructor instructor = new Instructor();
            instructor.setUser(user);
            instructor.setBio("");
            instructor.setExpertise("");
            instructorRepo.save(instructor);
        }
    }
}

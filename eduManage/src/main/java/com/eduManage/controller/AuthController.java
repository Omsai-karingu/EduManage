package com.eduManage.controller;

import com.eduManage.config.JwtUtil;
import com.eduManage.entity.Instructor;
import com.eduManage.entity.User;
import com.eduManage.repository.InstructorRepo;
import com.eduManage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    InstructorRepo instructrepo;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");
        String role = req.get("role");

        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));

        try {
            user.setRole(User.Role.valueOf(role.toUpperCase()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid role");
        }

        userRepo.save(user);

        // ✅ AUTO CREATE INSTRUCTOR
        if (user.getRole() == User.Role.INSTRUCTOR) {
            Instructor instructor = new Instructor();
            instructor.setUser(user);
            instructor.setBio("");
            instructor.setExpertise("");
            instructrepo.save(instructor);
        }

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}

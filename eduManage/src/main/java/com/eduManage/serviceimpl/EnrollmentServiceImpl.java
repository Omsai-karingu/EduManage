package com.eduManage.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.eduManage.config.Securityutil;
import com.eduManage.entity.*;
import com.eduManage.repository.*;
import com.eduManage.service.EnrollmentService;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired private EnrollmentRepository enrollmentRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private CourseRepository courseRepo;
    @Autowired private PaymentRepository paymentRepo;

    // ================= ENROLL =================
    @Override
    public ResponseEntity<?> enroll(int courseId) {

        String email = Securityutil.getCurrentUser();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.STUDENT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only students can enroll");
        }

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Prevent duplicate enrollment
        if (enrollmentRepo.existsByUser_IdAndCourse_Id(user.getId(), courseId)) {
            return ResponseEntity.badRequest()
                    .body("Already enrolled in this course");
        }


        // PAYMENT (mock success)
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(course.getPrice());
        payment.setStatus(Payment.Status.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepo.save(payment);

        // ENROLLMENT
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(Enrollment.Status.ACTIVE);

        enrollmentRepo.save(enrollment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Enrollment successful");
    }

    // ================= MY ENROLLMENTS =================
    @Override
    public ResponseEntity<?> getMyEnrollments() {

        String email = Securityutil.getCurrentUser();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Enrollment> enrollments =
                enrollmentRepo.findByUser_Id(user.getId());

        return ResponseEntity.ok(enrollments);
    }

    // ================= COURSE ENROLLMENTS =================
    @Override
    public ResponseEntity<?> getEnrollmentsByCourse(int courseId) {

        List<Enrollment> enrollments =
                enrollmentRepo.findByCourse_Id(courseId);

        return ResponseEntity.ok(enrollments);
    }
}

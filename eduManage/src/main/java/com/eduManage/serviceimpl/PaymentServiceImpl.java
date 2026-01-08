package com.eduManage.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eduManage.config.Securityutil;
import com.eduManage.entity.Course;
import com.eduManage.entity.Enrollment;
import com.eduManage.entity.Payment;
import com.eduManage.entity.User;
import com.eduManage.repository.CourseRepository;
import com.eduManage.repository.EnrollmentRepository;
import com.eduManage.repository.PaymentRepository;
import com.eduManage.repository.UserRepository;
import com.eduManage.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    // ================= MAKE PAYMENT =================
    @Override
    public ResponseEntity<?> makePayment(int courseId, String method) {

        String email = Securityutil.getCurrentUser();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // ❌ already enrolled?
        if (enrollmentRepo.existsByUser_IdAndCourse_Id(user.getId(), courseId)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Already enrolled in this course");
        }

        // ❌ already paid?
        if (paymentRepo.existsByUser_IdAndCourse_IdAndStatus(
                user.getId(), courseId, Payment.Status.SUCCESS)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Payment already completed");
        }

        // ✅ PAYMENT (mock success)
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(course.getPrice());
        payment.setStatus(Payment.Status.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepo.save(payment);

        // ✅ CREATE ENROLLMENT
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setStatus(Enrollment.Status.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());

        enrollmentRepo.save(enrollment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(payment);
    }

    // ================= MY PAYMENTS =================
    @Override
    public List<Payment> myPayments() {

        String email = Securityutil.getCurrentUser();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentRepo.findByUser_Id(user.getId());
    }

    // ================= ALL PAYMENTS (ADMIN) =================
    @Override
    public List<Payment> allPayments() {
        return paymentRepo.findAll();
    }
}

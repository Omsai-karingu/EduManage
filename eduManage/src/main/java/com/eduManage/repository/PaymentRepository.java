package com.eduManage.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eduManage.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByUser_Id(int userId);

    boolean existsByUser_IdAndCourse_IdAndStatus(
            int userId,
            int courseId,
            Payment.Status status
    );
}

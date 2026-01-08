package com.eduManage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduManage.entity.Payment;
import com.eduManage.service.PaymentService;
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/pay/{courseId}")
    public ResponseEntity<?> pay(@PathVariable int courseId,
                       @RequestParam String method) {
        return paymentService.makePayment(courseId, method);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my")
    public List<Payment> myPayments() {
        return paymentService.myPayments();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<Payment> allPayments() {
        return paymentService.allPayments();
    }
}

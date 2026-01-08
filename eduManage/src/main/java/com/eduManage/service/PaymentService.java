package com.eduManage.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eduManage.entity.Payment;

public interface PaymentService {
	ResponseEntity<?> makePayment(int courseId, String paymentMethod);
	
	List<Payment> myPayments();

    List<Payment> allPayments();	
}

package com.eduManage.service;


import org.springframework.http.ResponseEntity;



public interface EnrollmentService {

    ResponseEntity<?> enroll(int courseId);

    ResponseEntity<?> getMyEnrollments();

    ResponseEntity<?> getEnrollmentsByCourse(int courseId);
}

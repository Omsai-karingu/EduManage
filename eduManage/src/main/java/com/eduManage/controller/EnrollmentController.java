package com.eduManage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eduManage.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // STUDENT
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/course/{courseId}")
    public Object enroll(@PathVariable int courseId) {
        return enrollmentService.enroll(courseId);
    }

    // STUDENT
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my")
    public Object myEnrollments() {
        return enrollmentService.getMyEnrollments();
    }

    // ADMIN / INSTRUCTOR
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @GetMapping("/course/{courseId}")
    public Object courseEnrollments(@PathVariable int courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }
}

package com.eduManage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.eduManage.bean.CourseRequest;
import com.eduManage.entity.Course;
import com.eduManage.service.CourseService;



@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody CourseRequest request) {
        return courseService.createCourse(request);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Course> update(@PathVariable int id,
                                         @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
    	courseService.delete(id);
        return ResponseEntity.ok("Course deleted successfully");
    }
    
    @GetMapping("/all")
    public List<Course> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Course getByCourseId(@PathVariable int id) {
        return courseService.getById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Course> getByCategory(@PathVariable int categoryId) {
        return courseService.getByCategory(categoryId);
    }

    @GetMapping("/instructor/{instructorId}")
    public List<Course> getByInstructor(@PathVariable int instructorId) {
        return courseService.getByInstructor(instructorId);
    }
}

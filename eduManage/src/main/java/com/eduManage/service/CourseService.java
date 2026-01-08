package com.eduManage.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.eduManage.bean.CourseRequest;
import com.eduManage.entity.Course;

public interface CourseService {

	// ================= CREATE =================
	ResponseEntity<Object> createCourse(CourseRequest request);

	// ================= UPDATE =================
	ResponseEntity<Course> updateCourse(int courseId, CourseRequest request);

	// ================= READ =================
	List<Course> getAll();

	Course getById(int id);

	List<Course> getByCategory(int categoryId);

	List<Course> getByInstructor(int instructorId);

	void delete(int id);
}

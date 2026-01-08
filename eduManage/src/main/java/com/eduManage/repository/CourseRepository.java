package com.eduManage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eduManage.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer>{
	List<Course> findByCategory_Id(Integer categoryId);

    List<Course> findByInstructor_Id(Integer instructorId);
}

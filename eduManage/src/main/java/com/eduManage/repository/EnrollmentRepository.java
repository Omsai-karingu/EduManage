package com.eduManage.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import com.eduManage.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
	
	boolean existsByUser_IdAndCourse_Id(int userId, int courseId);

    List<Enrollment> findByUser_Id(int userId);

    List<Enrollment> findByCourse_Id(int courseId);
}

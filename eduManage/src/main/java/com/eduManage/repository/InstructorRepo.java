package com.eduManage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduManage.entity.Instructor;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, Integer> {
	Optional<Instructor> findByUser_Id(int userId);
}

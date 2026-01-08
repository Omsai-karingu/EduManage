package com.eduManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eduManage.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {}

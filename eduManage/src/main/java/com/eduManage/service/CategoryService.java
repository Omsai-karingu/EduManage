package com.eduManage.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eduManage.entity.Category;


public interface CategoryService {
	 // ========= CATEGORY MANAGEMENT =========

    // Admin
    ResponseEntity<Object> addCategory(Category category);

    // Public / User
    List<Category> getAll();

    Category getById(int id);

    // Admin
    Category update(int id, Category category);

    // Admin
    void delete(int id);
}

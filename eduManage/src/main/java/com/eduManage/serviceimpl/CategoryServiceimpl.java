package com.eduManage.serviceimpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eduManage.bean.CategoryResponse;
import com.eduManage.entity.Category;

import com.eduManage.repository.CategoryRepository;
import com.eduManage.service.CategoryService;

@Service
public class CategoryServiceimpl implements CategoryService {

	@Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<Object> addCategory(Category category) {
        Category saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryResponse("success", 201, saved, "Category added successfully"));
    }

    @Override
    public Category update(int id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    @Override
    public void delete(int id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(existing);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

}

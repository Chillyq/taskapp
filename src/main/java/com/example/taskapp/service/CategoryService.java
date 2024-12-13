package com.example.taskapp.service;

import com.example.taskapp.entity.Category;
import com.example.taskapp.repository.CategRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategRepo categRepo;

    @Autowired
    public CategoryService(CategRepo categRepo) {
        this.categRepo = categRepo;
    }

    public List<Category> getAllCategories() {
        return categRepo.findAll();
    }

    public Category getCategoryById(Long id) {
        return categRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

}

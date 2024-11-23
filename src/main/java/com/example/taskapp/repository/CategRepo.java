package com.example.taskapp.repository;

import com.example.taskapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategRepo extends JpaRepository<Category, Long> {
}

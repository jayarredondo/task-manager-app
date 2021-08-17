package com.highexpectations.taskmanagerapp.repositories;

import com.highexpectations.taskmanagerapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

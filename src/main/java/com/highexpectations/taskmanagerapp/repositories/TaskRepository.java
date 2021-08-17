package com.highexpectations.taskmanagerapp.repositories;

import com.highexpectations.taskmanagerapp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

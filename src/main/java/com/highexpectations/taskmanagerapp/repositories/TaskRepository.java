package com.highexpectations.taskmanagerapp.repositories;

import com.highexpectations.taskmanagerapp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(long id);
}

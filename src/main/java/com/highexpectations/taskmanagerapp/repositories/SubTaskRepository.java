package com.highexpectations.taskmanagerapp.repositories;

import com.highexpectations.taskmanagerapp.models.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    List<SubTask> findAllByTaskId(long id);
}

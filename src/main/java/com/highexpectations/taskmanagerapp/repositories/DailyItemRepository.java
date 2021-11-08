package com.highexpectations.taskmanagerapp.repositories;

import com.highexpectations.taskmanagerapp.models.DailyItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyItemRepository extends JpaRepository<DailyItem, Long> {
    List<DailyItem> findAllByUserId(long id);
}

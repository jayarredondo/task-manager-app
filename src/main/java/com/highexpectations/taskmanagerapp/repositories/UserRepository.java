package com.highexpectations.taskmanagerapp.repositories;

import com.highexpectations.taskmanagerapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

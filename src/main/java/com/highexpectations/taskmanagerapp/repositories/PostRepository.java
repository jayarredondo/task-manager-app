package com.highexpectations.taskmanagerapp.repositories;

import com.highexpectations.taskmanagerapp.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

package com.highexpectations.taskmanagerapp.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sub_tasks")
public class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 50, nullable = false)
    private String title;
    @Column(columnDefinition = "text", nullable = false)
    private String description;
    @Column(columnDefinition = "timestamp", nullable = false)
    private LocalDateTime createdAt;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime startDateTime;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    private boolean isComplete = false;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public SubTask() {
    }

    public SubTask(long id, String title, String description, LocalDateTime createdAt, LocalDateTime startDateTime, LocalDateTime endDateTime, Task task) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.task = task;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}

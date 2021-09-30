package com.highexpectations.taskmanagerapp.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "daily_items")
public class DailyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private boolean isComplete = false;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public DailyItem() {
    }

    public DailyItem(long id, String title, Date createdAt, boolean isComplete, User user) {
        this.title = title;
        this.isComplete = isComplete;
        this.user = user;
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

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

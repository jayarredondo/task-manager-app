package com.highexpectations.taskmanagerapp.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date remindMeAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reminder() {
    }

    public Reminder(long id, String title, Date createdAt, Date remindMeAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.remindMeAt = remindMeAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getRemindMeAt() {
        return remindMeAt;
    }

    public void setRemindMeAt(Date remindMeAt) {
        this.remindMeAt = remindMeAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

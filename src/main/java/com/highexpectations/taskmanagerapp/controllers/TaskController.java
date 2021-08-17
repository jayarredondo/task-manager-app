package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class TaskController {

    private final TaskRepository tasksDao;
    private final UserRepository usersDao;

    public TaskController(TaskRepository tasksDao, UserRepository usersDao) {
        this.tasksDao = tasksDao;
        this.usersDao = usersDao;
    }

    @GetMapping("/tasks")
    public String viewTasks(Model model) {
        model.addAttribute("tasks", tasksDao.findAll());
        return "tasks/index";
    }

    @GetMapping("/tasks/create")
    public String showCreateTasks(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/tasks/create")
    public String createTasks(@ModelAttribute Task task, @RequestParam String startDate, @RequestParam String endDate) {
        User user = usersDao.getById(1L);
        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());

        if(task.getStartDateTime() != null) {
            task.setStartDateTime(LocalDateTime.parse(startDate));
        }
        if(task.getEndDateTime() != null) {
            task.setEndDateTime(LocalDateTime.parse(endDate));
        }

        tasksDao.save(task);
        return "redirect:/tasks";
    }
}

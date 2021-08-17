package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.CategoryRepository;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
public class TaskController {

    private final TaskRepository tasksDao;
    private final UserRepository usersDao;
    private final CategoryRepository catDao;

    public TaskController(TaskRepository tasksDao, UserRepository usersDao, CategoryRepository catDao) {
        this.tasksDao = tasksDao;
        this.usersDao = usersDao;
        this.catDao = catDao;
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
    public String createTasks(@ModelAttribute Task task, @RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate", required = false) String endDate,
                              @RequestParam(name = "category", required = false) long cat_id) {
        User user = usersDao.getById(1L);
        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        if(!startDate.isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(startDate,formatter);
            task.setStartDateTime(start);

        }
        if(!endDate.isEmpty()) {
            LocalDateTime end = LocalDateTime.parse(endDate,formatter);
            task.setEndDateTime(end);
        }
        task.setCategory(catDao.getById(cat_id));

        tasksDao.save(task);
        return "redirect:/tasks";
    }
}

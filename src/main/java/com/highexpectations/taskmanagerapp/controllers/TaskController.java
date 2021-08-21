package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.CategoryRepository;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        User validUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(validUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("tasks", tasksDao.findAllByUserId(validUser.getId()));
        return "tasks/index";
    }

    @GetMapping("/tasks/create")
    public String showCreateTasks(Model model) {
        User validUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(validUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/tasks/create")
    public String createTasks(@ModelAttribute Task task, @RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate", required = false) String endDate,
                              @RequestParam(name = "category", required = false) long cat_id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setUser(currentUser);
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

    @GetMapping("/tasks/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        User validUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(validUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("task", tasksDao.getById(id));
        return "tasks/edit";
    }

    @PostMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable long id, @ModelAttribute Task task) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task taskFromDB = tasksDao.getById(id);
        if(loggedInUser.getId() == taskFromDB.getUser().getId()) {
            task.setCreatedAt(LocalDateTime.now());
            task.setUser(loggedInUser);
            tasksDao.save(task);
        }
        return "redirect:/tasks";
    }
}

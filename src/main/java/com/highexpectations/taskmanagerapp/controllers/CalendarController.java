package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarController {

    private final TaskRepository tasksDao;

    public CalendarController(TaskRepository tasksDao) {
        this.tasksDao = tasksDao;
    }

    @GetMapping("/calendar")
    public String showCalendar(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> allTasks = tasksDao.findAllByUserId(loggedInUser.getId());
        List<Task> scheduledTasks = new ArrayList<>();

        for(Task task : allTasks) {
            if(task.getStartDateTime() != null) {
                scheduledTasks.add(task);
            }
        }

        model.addAttribute("scheduledTasks", scheduledTasks);

        return "calendar";
    }
}

package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.CategoryRepository;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loggedInUser == null) {
            return "redirect:/login";
        }
        List<Task> allTasks = tasksDao.findAllByUserId(loggedInUser.getId());
        if(!allTasks.isEmpty()) {
            List<Task> unscheduledTasks = new ArrayList<>();
            List<Task> scheduledTasks = new ArrayList<>();
            List<Task> completedTasks = new ArrayList<>();
            for(Task task : allTasks){
                if(task.isComplete()) {
                    completedTasks.add(task);
                } else if (task.getStartDateTime() != null && !task.isComplete()){
                    scheduledTasks.add(task);
                } else if (!task.isComplete()){
                    unscheduledTasks.add(task);
                }
            }
            model.addAttribute("unscheduledTasks", unscheduledTasks);
            model.addAttribute("scheduledTasks", scheduledTasks);
            model.addAttribute("completedTasks", completedTasks);
        }
        return "tasks/index";
    }

    @GetMapping("/tasks/create")
    public String showCreateTasks(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/tasks/create")
    public String createTasks(@ModelAttribute Task task, @RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate", required = false) String endDate,
                              @RequestParam(name = "category", required = false) long cat_id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setUser(loggedInUser);
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
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loggedInUser == null) {
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


    @PostMapping("/tasks/{id}/complete")
    public String markTaskComplete(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Task taskToUpdate = tasksDao.getById(id);
        if(loggedInUser.getId() == taskToUpdate.getUser().getId()) {
            taskToUpdate.setCreatedAt(LocalDateTime.now());
            taskToUpdate.setUser(loggedInUser);
            taskToUpdate.setComplete(true);
            tasksDao.save(taskToUpdate);
        }

        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loggedInUser.getId() == tasksDao.getById(id).getUser().getId()) {
            tasksDao.delete(tasksDao.getById(id));
        }
        return "redirect:/tasks";
    }

    // SORTING TASKS

    @GetMapping("/tasks/today")
    public String showTasksForToday(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> allTasks = tasksDao.findAllByUserId(loggedInUser.getId());

        if(allTasks != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            List<Task> todaysTasks = new ArrayList<>();
            for (Task task : allTasks) {
                if(task.getStartDateTime() != null) {
                    if (task.getStartDateTime().getDayOfYear() == currentDate.getDayOfYear()) {
                        todaysTasks.add(task);
                    }
                }
            }
            model.addAttribute("todaysTasks", todaysTasks);
        }
        return "tasks/today";
    }
}

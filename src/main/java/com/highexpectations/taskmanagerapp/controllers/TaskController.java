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
import java.time.temporal.WeekFields;
import java.util.*;

@Controller
public class TaskController {

    private final TaskRepository tasksDao;
    private final UserRepository usersDao;
    private final CategoryRepository catDao;

//    public List<Task> sortByMostRecent(List<Task> unsortedTasks) {
//        List<Task> sortedTasks = new ArrayList<>();
//        for(Task task : unsortedTasks) {
//            int day = task.getCreatedAt().getDayOfYear();
//            int year = task.getCreatedAt().getYear();
//            if()
//        }
//        return sortedTasks;
//    }

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

        // sorting classes by to show most recent first
        Collections.sort(allTasks, new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                if (o1.getCreatedAt() == null || o2.getCreatedAt() == null)
                    return 0;
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });

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
            model.addAttribute("task", new Task());
            model.addAttribute("unscheduledTasks", unscheduledTasks);
            model.addAttribute("scheduledTasks", scheduledTasks);
            model.addAttribute("completedTasks", completedTasks);
        }
        return "tasks/index";
    }

    @GetMapping("/tasks/create")
    public String showCreateTasks(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("isCreate", true);
        return "tasks/create";
    }

    @PostMapping("/tasks/create")
    public String createTasks(@ModelAttribute Task task) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setUser(loggedInUser);
        task.setCreatedAt(LocalDateTime.now());

        tasksDao.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        model.addAttribute("task", tasksDao.getById(id));
        model.addAttribute("isCreate", false);
        return "tasks/create";
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
            // sorting tasks to show most recent
            Collections.sort(todaysTasks, new Comparator<Task>() {
                public int compare(Task o1, Task o2) {
                    if (o1.getCreatedAt() == null || o2.getCreatedAt() == null)
                        return 0;
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
            });
            model.addAttribute("todaysTasks", todaysTasks);
        }
        return "tasks/today";
    }

    @GetMapping("/tasks/this-week")
    public String showTasksForWeek(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> allTasks = tasksDao.findAllByUserId(loggedInUser.getId());
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        if(allTasks != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            int thisWeek = currentDate.get(weekFields.weekOfWeekBasedYear());
            List<Task> weeklyTasks = new ArrayList<>();
            for (Task task : allTasks) {
                if(task.getStartDateTime() != null) {
                    if (task.getStartDateTime().getDayOfYear() == currentDate.getDayOfYear() && task.getStartDateTime().get(weekFields.weekOfWeekBasedYear()) == thisWeek)
                    {
                        weeklyTasks.add(task);
                    }
                }
            }

            // sorting tasks to show most recent
            Collections.sort(allTasks, new Comparator<Task>() {
                public int compare(Task o1, Task o2) {
                    if (o1.getCreatedAt() == null || o2.getCreatedAt() == null)
                        return 0;
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
            });
            // sorting tasks to show most recent
            Collections.sort(weeklyTasks, new Comparator<Task>() {
                public int compare(Task o1, Task o2) {
                    if (o1.getCreatedAt() == null || o2.getCreatedAt() == null)
                        return 0;
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
            });
            model.addAttribute("weeklyTasks", weeklyTasks);
        }


        System.out.println();

        return "tasks/weekly";
    }
}

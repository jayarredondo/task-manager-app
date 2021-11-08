package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.SubTask;
import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.CategoryRepository;
import com.highexpectations.taskmanagerapp.repositories.SubTaskRepository;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class TaskController {

    private final TaskRepository tasksDao;
    private final SubTaskRepository subTasksDao;
    private final CategoryRepository catDao;

    public TaskController(TaskRepository tasksDao, SubTaskRepository subTasksDao, CategoryRepository catDao) {
        this.tasksDao = tasksDao;
        this.subTasksDao = subTasksDao;
        this.catDao = catDao;
    }

    @GetMapping("/tasks")
    public String viewTasks(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Task> allTasks = tasksDao.findAllByUserId(loggedInUser.getId());

        // sorting classes by to show most recent first
        // Collections.sort(allTasks, new Comparator<Task>() {
        // public int compare(Task o1, Task o2) {
        // if (o1.getCreatedAt() == null || o2.getCreatedAt() == null)
        // return 0;
        // return o1.getCreatedAt().compareTo(o2.getCreatedAt());
        // }
        // });

        if (!allTasks.isEmpty()) {
            List<Task> unscheduledTasks = new ArrayList<>();
            List<Task> scheduledTasks = new ArrayList<>();
            List<Task> completedTasks = new ArrayList<>();
            for (Task task : allTasks) {
                if (task.isComplete()) {
                    completedTasks.add(task);
                } else if (task.getStartDateTime() != null && !task.isComplete()) {
                    scheduledTasks.add(task);
                } else if (!task.isComplete()) {
                    unscheduledTasks.add(task);
                }
            }
            model.addAttribute("unscheduledTasks", unscheduledTasks);
            model.addAttribute("scheduledTasks", scheduledTasks);
            model.addAttribute("completedTasks", completedTasks);
        } else {
            List<Task> unscheduledTasks = new ArrayList<>();
            List<Task> scheduledTasks = new ArrayList<>();
            List<Task> completedTasks = new ArrayList<>();

            Task firstTask = new Task(0, "My First Task",
                    "This task will disappear after you've made your own task. You can edit the task, mark it as complete, or even add subtasks if necessary.",
                    LocalDateTime.now(), catDao.getById(8L));

            unscheduledTasks.add(firstTask);

            model.addAttribute("unscheduledTasks", unscheduledTasks);
            model.addAttribute("scheduledTasks", scheduledTasks);
            model.addAttribute("completedTasks", completedTasks);
        }
        return "tasks/indexTabView";
    }

    @GetMapping("/tasks/{id}")
    public String showTaskDetails(@PathVariable long id, Model model) {
        List<SubTask> subTasks = subTasksDao.findAllByTaskId(id);
        int completedTasks = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.isComplete()) {
                completedTasks++;
            }
        }
        if (completedTasks == subTasks.size()) {
            model.addAttribute("canMarkComplete", true);
            model.addAttribute("cannotMarkComplete", false);
        } else {
            model.addAttribute("cannotMarkComplete", true);
            model.addAttribute("canMarkComplete", false);
        }
        model.addAttribute("subTasks", subTasks);
        model.addAttribute("newSubTask", new SubTask());
        model.addAttribute("task", tasksDao.getById(id));
        return "tasks/show";
    }

    @GetMapping("/tasks/create")
    public String showCreateTasks(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userEmail", loggedInUser.getEmail());
        model.addAttribute("newTask", new Task());
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
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userEmail", loggedInUser.getEmail());
        model.addAttribute("task", tasksDao.getById(id));
        model.addAttribute("isCreate", false);
        return "tasks/create";
    }

    @PostMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable long id, @ModelAttribute Task task) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task taskFromDB = tasksDao.getById(id);
        if (loggedInUser.getId() == taskFromDB.getUser().getId()) {
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
        if (loggedInUser.getId() == taskToUpdate.getUser().getId()) {
            taskToUpdate.setCreatedAt(LocalDateTime.now());
            taskToUpdate.setUser(loggedInUser);
            taskToUpdate.setComplete(true);
            tasksDao.save(taskToUpdate);
        }

        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/reactivate")
    public String reactivateTask(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task taskToUpdate = tasksDao.getById(id);
        if (loggedInUser.getId() == taskToUpdate.getUser().getId()) {
            taskToUpdate.setCreatedAt(LocalDateTime.now());
            taskToUpdate.setUser(loggedInUser);
            taskToUpdate.setComplete(false);
            tasksDao.save(taskToUpdate);
        }

        return "redirect:/tasks/" + taskToUpdate.getId() + "/edit";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser.getId() == tasksDao.getById(id).getUser().getId()) {
            tasksDao.delete(tasksDao.getById(id));
        }
        return "redirect:/tasks";
    }

    // SORTING TASKS

    @GetMapping("/tasks/today")
    public String showTasksForToday(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> allTasks = tasksDao.findAllByUserId(loggedInUser.getId());

        if (allTasks != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            List<Task> todaysTasks = new ArrayList<>();
            for (Task task : allTasks) {
                if (task.getStartDateTime() != null) {
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

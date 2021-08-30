package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.SubTask;
import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.SubTaskRepository;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
public class SubTaskController {

    private final SubTaskRepository subTasksdao;
    private final TaskRepository tasksDao;

    public SubTaskController(SubTaskRepository subTasksdao, TaskRepository tasksDao) {
        this.subTasksdao = subTasksdao;
        this.tasksDao = tasksDao;
    }

    @GetMapping("/subTasks/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        model.addAttribute("subTask", subTasksdao.getById(id));
        return "subtasks/edit";
    }

    @PostMapping("/subTasks/{id}/edit")
    public String editSubTask(@PathVariable long id, @ModelAttribute SubTask subTask) {
        LocalDateTime currentDate = LocalDateTime.now();
        subTask.setTask(tasksDao.getById(id));
        subTask.setCreatedAt(currentDate);
        subTasksdao.save(subTask);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/subTasks/create")
    public String saveSubTask(@ModelAttribute SubTask subTask, @RequestParam(name= "taskID") long id) {
        LocalDateTime currentDate = LocalDateTime.now();
        subTask.setTask(tasksDao.getById(id));
        subTask.setCreatedAt(currentDate);
        subTasksdao.save(subTask);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/subTasks/{id}/delete")
    public String deleteSubTask(@PathVariable long id, @RequestParam(name = "taskID") long taskId) {
        subTasksdao.delete(subTasksdao.getById(id));
        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/subTasks/{id}/complete")
    public String markSubTaskComplete(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SubTask subTaskToUpdate = subTasksdao.getById(id);
        if(loggedInUser.getId() == subTaskToUpdate.getTask().getUser().getId()) {
            subTaskToUpdate.setCreatedAt(LocalDateTime.now());
            subTaskToUpdate.setComplete(true);
            subTasksdao.save(subTaskToUpdate);
        }

        return "redirect:/tasks/" + subTaskToUpdate.getTask().getId();
    }
}

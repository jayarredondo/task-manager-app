package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.CategoryRepository;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    private final TaskRepository tasksDao;
    private final CategoryRepository catDao;
    @Value("${OWAPI_KEY}")
    private String OWAPI_KEY;
//    @Value("${TWILIO_SID}")
//    private String TWILIO_SID;
//    @Value("${TWILIO_TOKEN}")
//    private String TWILIO_TOKEN;

    public DashboardController(TaskRepository tasksDao, CategoryRepository catDao) {
        this.tasksDao = tasksDao;
        this.catDao = catDao;
    }

    @GetMapping("/dashboard")
    private String showDashboard(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Task> allTasks = tasksDao.findAllByUserId(loggedInUser.getId());

        if(allTasks != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            List<Task> todaysTotalTasks = new ArrayList<>();
            List<Task> todaysCompleteTasks = new ArrayList<>();
            List<Task> todaysIncompleteTasks = new ArrayList<>();
            for (Task task : allTasks) {
                if(task.getStartDateTime() != null) {
                    if (task.getStartDateTime().getDayOfYear() == currentDate.getDayOfYear()) {
                        todaysTotalTasks.add(task);
                        if(task.isComplete()) {
                            todaysCompleteTasks.add(task);
                        } else {
                            todaysIncompleteTasks.add(task);
                        }
                    }
                }
            }
            // List of All Incomplete Tasks
            List<Task> workTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(5L), false);
            List<Task> healthTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(2L), false);
            List<Task> financialTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(1L), false);
            List<Task> socialTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(4L), false);
            List<Task> miscTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(8L), false);
            List<Task> choreTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(3L), false);
            List<Task> academicTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(6L), false);
            List<Task> hobbyTasks = tasksDao.findAllByUserIdAndCategoryAndIsComplete(loggedInUser.getId(), catDao.getById(7L), false);


            model.addAttribute("workTasks", workTasks.size());
            model.addAttribute("healthTasks", healthTasks.size());
            model.addAttribute("financialTasks", financialTasks.size());
            model.addAttribute("socialTasks", socialTasks.size());
            model.addAttribute("miscTasks", miscTasks.size());
            model.addAttribute("familyTasks", choreTasks.size());
            model.addAttribute("academicTasks", academicTasks.size());
            model.addAttribute("hobbyTasks", hobbyTasks.size());
            model.addAttribute("todaysTasks", todaysTotalTasks);
            model.addAttribute("todaysIncompleteTasks", todaysIncompleteTasks);
            model.addAttribute("todaysCompleteTasks", todaysCompleteTasks);
            model.addAttribute("iTaskSize", todaysIncompleteTasks.size());
            model.addAttribute("cTaskSize", todaysCompleteTasks.size());
        }
        model.addAttribute("OWAPI_KEY", OWAPI_KEY);
        model.addAttribute("currentDate", LocalDateTime.now());
        model.addAttribute("currentUser", loggedInUser);
        return "dashboard/index";
    }

    @GetMapping("/pomodoro")
    public String showPomodoro() {
        return "pomodoro-solo";
    }

//    @GetMapping("/twilio")
//    public String sendSMS() {
//        Twilio.init(TWILIO_SID, TWILIO_TOKEN);
//        Message message = Message.creator(
//                new com.twilio.type.PhoneNumber("+12102485536"),
//                new com.twilio.type.PhoneNumber("+13176612879"),
//                "Hey cheeks, it's Jay. Just testing out the texting service.")
//                .create();
//        return "test";
//    }
}

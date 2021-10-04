package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.DailyItem;
import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.CategoryRepository;
import com.highexpectations.taskmanagerapp.repositories.DailyItemRepository;
import com.highexpectations.taskmanagerapp.repositories.TaskRepository;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
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
    private final DailyItemRepository dailyItemsDao;
    private final UserRepository usersDao;
//    @Value("${TWILIO_SID}")
//    private String TWILIO_SID;
//    @Value("${TWILIO_TOKEN}")
//    private String TWILIO_TOKEN;

    public DashboardController(TaskRepository tasksDao, CategoryRepository catDao, DailyItemRepository dailyItemsDao, UserRepository usersDao) {
        this.tasksDao = tasksDao;
        this.catDao = catDao;
        this.dailyItemsDao = dailyItemsDao;
        this.usersDao = usersDao;
    }

    @GetMapping("/dashboard")
    private String showDashboard(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<DailyItem> dailyItems = dailyItemsDao.findAllByUserId(loggedInUser.getId());

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
            model.addAttribute("choreTasks", choreTasks.size());
            model.addAttribute("academicTasks", academicTasks.size());
            model.addAttribute("hobbyTasks", hobbyTasks.size());
            model.addAttribute("todaysTasks", todaysTotalTasks);
            model.addAttribute("todaysIncompleteTasks", todaysIncompleteTasks);
            model.addAttribute("todaysCompleteTasks", todaysCompleteTasks);
            model.addAttribute("iTaskSize", todaysIncompleteTasks.size());
            model.addAttribute("cTaskSize", todaysCompleteTasks.size());
        }
        model.addAttribute("dailyItems", dailyItems);
        model.addAttribute("currentDate", LocalDateTime.now());
        model.addAttribute("currentUser", usersDao.getById(loggedInUser.getId()));
        return "dashboard/index";
    }
    @GetMapping("/pomodoro")
    public String showPomodoro() {
        return "pomodoro-solo";
    }

}

package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.DailyItem;
import com.highexpectations.taskmanagerapp.models.Note;
import com.highexpectations.taskmanagerapp.models.Task;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository users;
    private final TaskRepository taskDao;
    private final CategoryRepository catDao;
    private final NoteRepository notesDao;
    private final PasswordEncoder passwordEncoder;
    private final DailyItemRepository dailyItemDao;

    public UserController(CategoryRepository catDao, TaskRepository taskDao, NoteRepository notesDao, UserRepository users, DailyItemRepository dailyItemDao, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.taskDao = taskDao;
        this.catDao = catDao;
        this.notesDao = notesDao;
        this.dailyItemDao = dailyItemDao;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping(method = RequestMethod.GET, path = "/sign-up")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setUsername(user.getEmail());
        user.setPassword(hash);
        users.save(user);

        Task firstTask = new Task("My First Task", "This task will disappear after you've made your own task. You can edit the task, mark it as complete, or even add subtasks if necessary.", LocalDateTime.now(), catDao.getById(8L), user);
        Note firstNote = new Note("Welcome to BackUp Brain!", "This message will be deleted after you've created your first note. Click on the Write button above to create a new note! All of your notes will be displayed here, with the latest note appearing at the top. Enjoy your space to write whatever you need!", LocalDateTime.now(), catDao.getById(8L), user);

        taskDao.save(firstTask);
        notesDao.save(firstNote);

        return "redirect:/login";
    }

    @GetMapping("/settings")
    public String showSettings(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<DailyItem> dailyItemList = dailyItemDao.findAllByUserId(loggedInUser.getId());

        model.addAttribute("newDailyItem", new DailyItem());
        model.addAttribute("dailyItems", dailyItemList);
        model.addAttribute("user", users.getById(loggedInUser.getId()));
        return "auth/settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = users.getById(loggedInUser.getId());
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        users.save(u);
        return "redirect:/dashboard";
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable long id) {
        users.delete(users.getById(id));
        return "redirect:/";
    }
}

package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.DailyItem;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.DailyItemRepository;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final DailyItemRepository dailyItemDao;

    public UserController(UserRepository users, DailyItemRepository dailyItemDao, PasswordEncoder passwordEncoder) {
        this.users = users;
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
}

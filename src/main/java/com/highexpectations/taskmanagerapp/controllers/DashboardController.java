package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    private String showDashboard(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentDate", LocalDateTime.now());
        model.addAttribute("currentUser", loggedInUser);
        return "dashboard/index";
    }
}

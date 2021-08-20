package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    private String showDashboard() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loggedInUser == null) {
            return "redirect:/login";
        }
        return "dashboard/index";
    }
}

package com.highexpectations.taskmanagerapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    private String showDashboard() {
        return "dashboard/index";
    }
}

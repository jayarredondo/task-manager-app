package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.DailyItem;
import com.highexpectations.taskmanagerapp.repositories.DailyItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DailyItemController {
    private final DailyItemRepository dailyItemDao;

    public DailyItemController(DailyItemRepository dailyItemDao) {
        this.dailyItemDao = dailyItemDao;
    }

    @PostMapping("/dailyItems/{id}/complete")
    public String finishDailyItem(@PathVariable long id, @RequestParam(name = "dailyItem") boolean completed) {
        DailyItem itemChecked = dailyItemDao.getById(id);
        itemChecked.setComplete(completed);
        dailyItemDao.save(itemChecked);

        return "redirect:/dashboard";
    }
}

package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.DailyItem;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.DailyItemRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DailyItemController {
    private final DailyItemRepository dailyItemDao;

    public DailyItemController(DailyItemRepository dailyItemDao) {
        this.dailyItemDao = dailyItemDao;
    }

    @PostMapping("/dailyItems/{id}/complete")
    public String finishDailyItem(@PathVariable long id, @RequestParam(name = "dailyItem") String completed) {
        DailyItem itemChecked = dailyItemDao.getById(id);
        if(completed.equals("false")) {
            itemChecked.setComplete(true);
        } else {
            itemChecked.setComplete(false);
        }
        dailyItemDao.save(itemChecked);

        return "redirect:/dashboard";
    }

    @PostMapping("/reset/dailyItems")
    public String resetDailyItems() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<DailyItem> userItems = dailyItemDao.findAllByUserId(loggedInUser.getId());

        for( DailyItem item : userItems) {
            item.setComplete(false);
            dailyItemDao.save(item);
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/dailyItems/{id}/delete")
    public String deleteDailyItem(@PathVariable long id) {
        dailyItemDao.delete(dailyItemDao.getById(id));
        return "redirect:/settings";
    }

    @PostMapping("dailyItems/create")
    public String createDailyItem(@ModelAttribute DailyItem dailyItem) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dailyItem.setUser(loggedInUser);
        dailyItemDao.save(dailyItem);
        return "redirect:/settings";
    }
}

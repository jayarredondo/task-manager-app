package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Note;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.NoteRepository;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class NotesController {

    private final NoteRepository notesDao;
    private final UserRepository usersDao;

    public NotesController(NoteRepository notesDao, UserRepository usersDao) {
        this.notesDao = notesDao;
        this.usersDao = usersDao;
    }

    @GetMapping("/notes")
    public String showNotes(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Note note = notesDao.findAllByUserId(loggedInUser.getId()).get(0);
        LocalDateTime dt = note.getCreatedAt();
        System.out.println(dt.getDayOfWeek());

        model.addAttribute("notes", notesDao.findAllByUserId(loggedInUser.getId()));
        return "notes/index";
    }

    @GetMapping("/notes/create")
    public String showNotesCreate(Model model) {
        User validUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(validUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("note", new Note());
        return "notes/create";
    }

    @PostMapping("/notes/create")
    public String createNote(@ModelAttribute Note note) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(loggedInUser == null) {
            return "redirect:/login";
        }
        note.setUser(loggedInUser);
        note.setCreatedAt(LocalDateTime.now());
        notesDao.save(note);
        return "redirect:/notes";
    }
}

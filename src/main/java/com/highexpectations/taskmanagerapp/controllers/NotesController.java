package com.highexpectations.taskmanagerapp.controllers;

import com.highexpectations.taskmanagerapp.models.Note;
import com.highexpectations.taskmanagerapp.models.User;
import com.highexpectations.taskmanagerapp.repositories.CategoryRepository;
import com.highexpectations.taskmanagerapp.repositories.NoteRepository;
import com.highexpectations.taskmanagerapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class NotesController {

    private final NoteRepository notesDao;
    private final UserRepository usersDao;
    private final CategoryRepository catDao;

    public NotesController(NoteRepository notesDao, UserRepository usersDao, CategoryRepository catDao) {
        this.notesDao = notesDao;
        this.usersDao = usersDao;
        this.catDao = catDao;
    }

    @GetMapping("/notes")
    public String showNotes(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Note firstNote = new Note(0, "Welcome to BackUp Brain!",
                "This message will be deleted after you've created your first note. Click on the Write button above to create a new note! All of your notes will be displayed here, with the latest note appearing at the top. Enjoy your space to write whatever you need!",
                LocalDateTime.now(), catDao.getById(8L));

        List<Note> startUpNotes = new ArrayList<>();
        startUpNotes.add(firstNote);

        if (notesDao.findAllByUserId(loggedInUser.getId()).size() > 0) {
            model.addAttribute("notes", notesDao.findAllByUserId(loggedInUser.getId()));
        } else {
            model.addAttribute("notes", startUpNotes);
        }

        return "notes/index";
    }

    @GetMapping("/notes/create")
    public String showNotesCreate(Model model) {
        model.addAttribute("newNote", new Note());
        model.addAttribute("isCreate", true);
        return "notes/create";
    }

    @PostMapping("/notes/create")
    public String createNote(@ModelAttribute Note note) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        note.setUser(loggedInUser);
        note.setCreatedAt(LocalDateTime.now());
        notesDao.save(note);
        return "redirect:/notes";
    }

    @GetMapping("/notes/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model) {
        model.addAttribute("note", notesDao.getById(id));
        model.addAttribute("isCreate", false);
        return "notes/create";
    }

    @PostMapping("/notes/{id}/edit")
    public String editTask(@PathVariable long id, @ModelAttribute Note note) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Note noteFromDB = notesDao.getById(id);
        if (loggedInUser.getId() == noteFromDB.getUser().getId()) {
            note.setCreatedAt(LocalDateTime.now());
            note.setUser(loggedInUser);
            notesDao.save(note);
        }
        return "redirect:/notes";
    }

    @PostMapping("/notes/{id}/delete")
    public String deleteTask(@PathVariable long id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser.getId() == notesDao.getById(id).getUser().getId()) {
            notesDao.delete(notesDao.getById(id));
        }
        return "redirect:/notes";
    }
}

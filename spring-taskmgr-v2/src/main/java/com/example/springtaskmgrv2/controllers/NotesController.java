package com.example.springtaskmgrv2.controllers;

import com.example.springtaskmgrv2.entities.ErrorResponse;
import com.example.springtaskmgrv2.entities.NoteEntity;
import com.example.springtaskmgrv2.services.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    //    @Autowired
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping("") // GET /notes
    public List<NoteEntity> getAllNotes() {
        return notesService.findAllNotes();
    }

    @GetMapping("/{id}") // GET /notes/1
    public NoteEntity getNoteById(@PathVariable String id) {
        return notesService.findNoteById(id);
    }

    @ExceptionHandler(NotesService.NoteNotFoundException.class)
    ResponseEntity<ErrorResponse> handleErrors(NotesService.NoteNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}

package com.example.springtaskmgrv2.services;

import com.example.springtaskmgrv2.entities.NoteEntity;
import com.example.springtaskmgrv2.repositories.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    public List<NoteEntity> findAllNotes() {
        return notesRepository.findAll();
    }

    public static class NoteNotFoundException extends IllegalArgumentException {
        public NoteNotFoundException(String id) {
            super("Note with id [" + id + "] not found");
        }
    }

    public NoteEntity findNoteById(String id) {
        return notesRepository.findById(Integer.parseInt(id)).orElseThrow(() -> new NoteNotFoundException(id));
    }
}

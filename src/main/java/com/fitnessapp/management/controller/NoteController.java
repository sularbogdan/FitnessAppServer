package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.NoteDTO;
import com.fitnessapp.management.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @PostMapping("/user/{userId}")
    public ResponseEntity<NoteDTO> createNote(@PathVariable Long userId, @RequestBody NoteDTO dto) {
        NoteDTO createdNote = noteService.createNote(userId, dto);
        return ResponseEntity.ok(createdNote);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteDTO>> getNotesForUser(@PathVariable Long userId) {
        List<NoteDTO> notes = noteService.getNotesForUser(userId);
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/user/{userId}/note/{noteId}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long userId,
                                              @PathVariable Long noteId,
                                              @RequestBody NoteDTO dto) {
        NoteDTO updatedNote = noteService.updateNote(userId, noteId, dto);
        return ResponseEntity.ok(updatedNote);
    }


    @DeleteMapping("/user/{userId}/note/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long userId, @PathVariable Long noteId) {
        noteService.deleteNote(userId, noteId);
        return ResponseEntity.ok().build();
    }
}
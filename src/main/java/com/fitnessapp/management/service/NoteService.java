package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.dto.NoteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    NoteDTO createNote(Long userId, NoteDTO dto);
    List<NoteDTO> getNotesForUser(Long userId);
    NoteDTO updateNote(Long userId, Long noteId, NoteDTO dto);
    void deleteNote(Long userId, Long noteId);
}

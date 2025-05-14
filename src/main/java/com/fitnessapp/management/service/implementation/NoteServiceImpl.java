package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.config.MapperConfig;
import com.fitnessapp.management.exception.NoteNotFindException;
import com.fitnessapp.management.exception.UserNotFoundException;
import com.fitnessapp.management.repository.NoteRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.NoteDTO;
import com.fitnessapp.management.repository.entity.Note;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.NoteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final MapperConfig mapper;

    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository, MapperConfig mapper) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public NoteDTO createNote(Long userId, NoteDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note note = new Note();
        note.setSubject(dto.getSubject());
        note.setContent(dto.getContent());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setUser(user);

        return mapper.mapToDto(noteRepository.save(note), NoteDTO.class);
    }

    public List<NoteDTO> getNotesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Note> notes = noteRepository.findByUserId(user.getId());
        return mapper.mapToList(notes, NoteDTO.class);
    }

    public NoteDTO updateNote(Long userId, Long noteId, NoteDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFindException("Note not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User not authorized to update this note");
        }

        note.setSubject(dto.getSubject());
        note.setContent(dto.getContent());
        note.setUpdatedAt(LocalDateTime.now());

        return mapper.mapToDto(noteRepository.save(note), NoteDTO.class);
    }

    public void deleteNote(Long userId, Long noteId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFindException("Note not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User not authorized to delete this note");
        }

        noteRepository.delete(note);
    }
}



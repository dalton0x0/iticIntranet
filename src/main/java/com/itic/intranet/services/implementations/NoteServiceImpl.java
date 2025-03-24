package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.Note;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.services.NoteService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllNotes() {
        List<Note> allNotes = noteRepository.findAll();
        return allNotes.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("No notes found", allNotes))
                : ResponseEntity.ok(new ApiResponse("List of notes", allNotes));
    }

    @Override
    public ResponseEntity<ApiResponse> addNote(NoteRequestDto noteDto, User user, Evaluation evaluation) {
        if (noteDto.getValue() < 0) {
            throw new BadRequestException("Invalid value");
        }
        Note newNote = Note.builder()
                .value(noteDto.getValue())
                .user(user)
                .evaluation(evaluation)
                .build();
        Note savedNote = noteRepository.save(newNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Note created successfully", savedNote));
    }

    @Override
    public ResponseEntity<ApiResponse> updateNote(Long id, NoteRequestDto noteDto) {
        return noteRepository.findById(id)
                .map(existingNote -> {
                    existingNote.setValue(noteDto.getValue());
                    Note updatedNote = noteRepository.save(existingNote);
                    return ResponseEntity.ok(new ApiResponse("Note updated successfully", updatedNote));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteNote(Long id) {
        return noteRepository.findById(id)
                .map(note -> {
                    noteRepository.delete(note);
                    return ResponseEntity.ok(new ApiResponse("Note deleted successfully", null));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + id));
    }
}

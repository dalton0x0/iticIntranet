package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.Note;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.services.NoteService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Override
    public ApiResponse getAllNotes() {
        List<Note> allNotes = noteRepository.findAll();
        if (allNotes.isEmpty()) {
            return new ApiResponse("List of notes is empty", HttpStatus.NO_CONTENT, allNotes);
        }
        return new ApiResponse("List of notes", HttpStatus.OK, allNotes);
    }

    @Override
    public ApiResponse addNote(NoteRequestDto noteDto, User user, Evaluation evaluation) {
        var newNote = Note.builder()
                .value(noteDto.getValue())
                .user(user)
                .evaluation(evaluation)
                .build();
        Note savedNote = noteRepository.save(newNote);
        return new ApiResponse("Note created successfully", HttpStatus.CREATED, savedNote);
    }

    @Override
    public ApiResponse updateNote(Long id, NoteRequestDto noteDto) {
        Note existingNote = noteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Note not found")
        );
        existingNote.setValue(noteDto.getValue());
        Note updatedNote = noteRepository.save(existingNote);
        return new ApiResponse("Note updated successfully", HttpStatus.OK, updatedNote);
    }

    @Override
    public ApiResponse deleteNote(Long id) {
        Note noteToDelete = noteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Note not found")
        );
        noteRepository.delete(noteToDelete);
        return new ApiResponse("Note deleted successfully", HttpStatus.OK, null);
    }
}

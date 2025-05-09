package com.itic.intranet.web;

import com.itic.intranet.dtos.NoteMinimalDto;
import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.models.Note;
import com.itic.intranet.services.NoteService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v18/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> getAllNotes() {
        List<NoteResponseDto> notes = noteService.getAllNotes();
        return notes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@RequestBody NoteRequestDto noteDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(noteDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(@PathVariable Long id, @RequestBody NoteRequestDto noteDto) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(id, noteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/evaluation/{evaluationId}")
    public ResponseEntity<List<NoteResponseDto>> getNotesByEvaluation(@PathVariable Long evaluationId) {
        List<NoteResponseDto> notes = noteService.getNotesByEvaluation(evaluationId);
        return notes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notes);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<NoteMinimalDto>> getNotesByStudent(@PathVariable Long studentId) {
        List<NoteMinimalDto> notes = noteService.getNotesByStudent(studentId);
        return notes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notes);
    }
}

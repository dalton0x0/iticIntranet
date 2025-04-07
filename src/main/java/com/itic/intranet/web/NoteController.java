package com.itic.intranet.web;

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
@RequestMapping("/api/v7/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public ResponseEntity<List<NoteResponseDto>> getNoteById(@PathVariable Long id) {
        List<NoteResponseDto> notes = noteService.getAllNotes();
        return notes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notes);
    }

    @PostMapping("/all")
    public ResponseEntity<NoteResponseDto> createNote(@RequestBody NoteRequestDto noteDto) {
        NoteResponseDto noteResponseDto = noteService.createNote(noteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponseDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(@PathVariable Long id, @RequestBody NoteRequestDto noteDto) {
        NoteResponseDto noteResponseDto = noteService.updateNote(id, noteDto);
        return noteResponseDto == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(noteResponseDto);
    }

    @DeleteMapping("/delete/{id}")
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
    public ResponseEntity<List<NoteResponseDto>> getNotesByStudent(@PathVariable Long studentId) {
        List<NoteResponseDto> notes = noteService.getNotesByStudent(studentId);
        return notes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notes);
    }
}
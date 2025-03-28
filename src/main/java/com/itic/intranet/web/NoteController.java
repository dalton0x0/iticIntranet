package com.itic.intranet.web;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.services.NoteService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse> createNote(@RequestBody NoteRequestDto noteDto) {
        ApiResponse response = noteService.createNote(noteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateNote(@PathVariable Long id, @RequestBody NoteRequestDto noteDto) {
        return ResponseEntity.ok(noteService.updateNote(id, noteDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.deleteNote(id));
    }

    @GetMapping("/evaluation/{evaluationId}")
    public ResponseEntity<ApiResponse> getNotesByEvaluation(@PathVariable Long evaluationId) {
        return ResponseEntity.ok(noteService.getNotesByEvaluation(evaluationId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse> getNotesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(noteService.getNotesByStudent(studentId));
    }
}
package com.itic.intranet.web;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.User;
import com.itic.intranet.services.NoteService;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/note")
@CrossOrigin(origins = "http://localhost:63343")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(new ApiResponse("API TEST OK !", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllNotes() {
        return noteService.getAllNotes();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addNote(@RequestBody NoteRequestDto noteDto, User user, Evaluation evaluation) {
        return noteService.addNote(noteDto, user, evaluation);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateNote(@PathVariable Long id, @RequestBody NoteRequestDto noteDto) {
        return noteService.updateNote(id, noteDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteNote(@PathVariable Long id) {
        return noteService.deleteNote(id);
    }
}

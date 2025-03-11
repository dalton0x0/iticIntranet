package com.itic.intranet.web;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.User;
import com.itic.intranet.services.NoteService;
import com.itic.intranet.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/note")
@CrossOrigin(origins = "http://localhost:63343")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/test")
    public ApiResponse test() {
        return new ApiResponse("API TEST OK !", HttpStatus.OK, null);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addNote(@Valid @RequestBody NoteRequestDto noteDto, User user, Evaluation evaluation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.addNote(noteDto, user, evaluation));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateNote(@PathVariable Long id,@Valid @RequestBody NoteRequestDto noteDto) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(id, noteDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteNote(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.deleteNote(id));
    }
}

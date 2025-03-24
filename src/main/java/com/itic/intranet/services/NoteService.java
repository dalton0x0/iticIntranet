package com.itic.intranet.services;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.User;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface NoteService {
    ResponseEntity<ApiResponse> getAllNotes();
    ResponseEntity<ApiResponse> addNote(NoteRequestDto noteDto, User user, Evaluation evaluation);
    ResponseEntity<ApiResponse> updateNote(Long id, NoteRequestDto noteDto);
    ResponseEntity<ApiResponse> deleteNote(Long id);
}

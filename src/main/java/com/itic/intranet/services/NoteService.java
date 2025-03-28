package com.itic.intranet.services;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface NoteService {
    ApiResponse getNoteById(Long id);
    ApiResponse createNote(NoteRequestDto noteDto);
    ApiResponse updateNote(Long id, NoteRequestDto noteDto);
    ApiResponse deleteNote(Long id);
    ApiResponse getNotesByEvaluation(Long evaluationId);
    ApiResponse getNotesByStudent(Long studentId);
}
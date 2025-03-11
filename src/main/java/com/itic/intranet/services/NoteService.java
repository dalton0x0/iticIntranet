package com.itic.intranet.services;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.User;
import com.itic.intranet.utils.ApiResponse;

public interface NoteService {
    ApiResponse getAllNotes();
    ApiResponse addNote(NoteRequestDto noteDto, User user, Evaluation evaluation);
    ApiResponse updateNote(Long id, NoteRequestDto noteDto);
    ApiResponse deleteNote(Long id);
}

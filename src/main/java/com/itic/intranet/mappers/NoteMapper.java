package com.itic.intranet.mappers;

import com.itic.intranet.dtos.NoteMinimalDto;
import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.models.mysql.Evaluation;
import com.itic.intranet.models.mysql.Note;
import com.itic.intranet.models.mysql.User;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public Note convertDtoToEntity(NoteRequestDto noteDto, User user, Evaluation evaluation) {
        return Note.builder()
                .id(noteDto.getId())
                .value(noteDto.getValue())
                .student(user)
                .evaluation(evaluation)
                .build();
    }

    public NoteResponseDto convertEntityToResponseDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .value(note.getValue())
                .studentId(note.getStudent().getId())
                .studentName(note.getStudent().getFirstName() + " " + note.getStudent().getLastName())
                .evaluationId(note.getEvaluation().getId())
                .evaluationTitle(note.getEvaluation().getTitle())
                .build();
    }

    public void updateEntityFromDto(NoteRequestDto noteDto, Note note, User user, Evaluation evaluation) {
        note.setValue(noteDto.getValue());
        note.setStudent(user);
        note.setEvaluation(evaluation);
    }

    public NoteMinimalDto convertEntityToUserMinimalDto(Note note) {
        return new NoteMinimalDto(note.getId(), note.getValue(), note.getEvaluation().getTitle());
    }
}

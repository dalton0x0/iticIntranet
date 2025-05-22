package com.itic.intranet.services;

import com.itic.intranet.dtos.NoteResponseDto;

import java.util.List;

public interface EvaluationPropertyService {
    void addClassroomToEvaluation(Long evaluationId, Long classroomId);
    void removeClassroomToEvaluation(Long evaluationId, Long classroomId);

    List<NoteResponseDto> getEvaluationNotes(Long evaluationId);
}

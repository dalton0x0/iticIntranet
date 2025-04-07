package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.models.Evaluation;

import java.util.List;

public interface EvaluationService {
    List<EvaluationDetailedResponseDto> getAllEvaluations();
    Evaluation getEvaluationById(Long id);
    List<Evaluation> searchEvaluations(String title);
    Evaluation createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId);
    Evaluation updateEvaluation(Long id, EvaluationRequestDto evaluationDto);
    void deleteEvaluation(Long id);
    void addClassroomToEvaluation(Long evaluationId, Long classroomId);
    void removeClassroomToEvaluation(Long evaluationId, Long classroomId);
}
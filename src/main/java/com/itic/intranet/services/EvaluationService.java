package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.models.Evaluation;

import java.util.List;

public interface EvaluationService {
    List<Evaluation> getAllEvaluations();
    EvaluationDetailedResponseDto getEvaluationById(Long id);
    Evaluation createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId);
    Evaluation updateEvaluation(Long id, EvaluationRequestDto evaluationDto);
    void deleteEvaluation(Long id);
    Evaluation addClassroomToEvaluation(Long evaluationId, Long classroomId);
}
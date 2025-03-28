package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface EvaluationService {
    ApiResponse getAllEvaluations();
    ApiResponse getEvaluationById(Long id);
    ApiResponse createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId);
    ApiResponse updateEvaluation(Long id, EvaluationRequestDto evaluationDto);
    ApiResponse deleteEvaluation(Long id);
    ApiResponse addClassroomToEvaluation(Long evaluationId, Long classroomId);
    ApiResponse getEvaluationResults(Long evaluationId);
}
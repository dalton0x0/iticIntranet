package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface EvaluationService {
    ApiResponse getAllEvaluations();
    ApiResponse getEvaluationById(Long id);
    ApiResponse getEvaluationByTitle(String title);
    ApiResponse addEvaluation(EvaluationRequestDto evaluationDto);
    ApiResponse updateEvaluation(Long id, EvaluationRequestDto evaluationDto);
    ApiResponse deleteEvaluation(Long id);
}

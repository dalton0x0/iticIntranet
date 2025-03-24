package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface EvaluationService {
    ResponseEntity<ApiResponse> getAllEvaluations();
    ResponseEntity<ApiResponse> getEvaluationById(Long id);
    ResponseEntity<ApiResponse> getEvaluationByTitle(String title);
    ResponseEntity<ApiResponse> addEvaluation(EvaluationRequestDto evaluationDto);
    ResponseEntity<ApiResponse> updateEvaluation(Long id, EvaluationRequestDto evaluationDto);
    ResponseEntity<ApiResponse> deleteEvaluation(Long id);
}

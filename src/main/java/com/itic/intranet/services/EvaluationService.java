package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;

import java.util.List;

public interface EvaluationService {
    List<EvaluationDetailedResponseDto> getAllEvaluations();
    EvaluationResponseDto getEvaluationById(Long id);
    List<EvaluationResponseDto> searchEvaluations(String title);
    EvaluationResponseDto createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId);
    EvaluationResponseDto updateEvaluation(Long id, EvaluationRequestDto evaluationDto);
    void deleteEvaluation(Long id);
    void finishEvaluation(Long id);
}

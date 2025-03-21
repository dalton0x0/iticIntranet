package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.services.EvaluationService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    EvaluationRepository evaluationRepository;

    @Override
    public ApiResponse getAllEvaluations() {
        List<Evaluation> allEvaluations = evaluationRepository.findAll();
        if (allEvaluations.isEmpty()) {
            return new ApiResponse("List of all evaluations is empty", HttpStatus.NO_CONTENT, allEvaluations);
        }
        return new ApiResponse("List of all evaluations", HttpStatus.OK, allEvaluations);
    }

    @Override
    public ApiResponse getEvaluationById(Long id) {
        Optional<Evaluation> evaluation = Optional.ofNullable(evaluationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        ));
        return new ApiResponse("Evaluation found", HttpStatus.OK, evaluation);
    }

    @Override
    public ApiResponse getEvaluationByTitle(String title) {
        Optional<Evaluation> classroom = Optional.ofNullable(evaluationRepository.findByTitleContaining(title).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        ));
        return new ApiResponse("Evaluation found", HttpStatus.OK, classroom);
    }

    @Override
    public ApiResponse addEvaluation(EvaluationRequestDto evaluationDto) {
        var newEvaluation = Evaluation.builder()
                .title(evaluationDto.getTitle())
                .description(evaluationDto.getDescription())
                .minValue(evaluationDto.getMinValue())
                .maxValue(evaluationDto.getMaxValue())
                .date(LocalDateTime.now())
                .users(evaluationDto.getUsers())
                .build();
        Evaluation savedEvaluation = evaluationRepository.save(newEvaluation);
        return new ApiResponse("Evaluation created successfully", HttpStatus.CREATED, savedEvaluation);
    }

    @Override
    public ApiResponse updateEvaluation(Long id, EvaluationRequestDto evaluationDto) {
        Evaluation existingEvaluation = evaluationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        );
        existingEvaluation.setTitle(evaluationDto.getTitle());
        existingEvaluation.setDescription(evaluationDto.getDescription());
        existingEvaluation.setMinValue(evaluationDto.getMinValue());
        existingEvaluation.setMaxValue(evaluationDto.getMaxValue());
        existingEvaluation.setDate(LocalDateTime.now());
        existingEvaluation.setUsers(evaluationDto.getUsers());
        Evaluation updatedEvaluation = evaluationRepository.save(existingEvaluation);
        return new ApiResponse("Evaluation updated successfully", HttpStatus.OK, updatedEvaluation);
    }

    @Override
    public ApiResponse deleteEvaluation(Long id) {
        Evaluation evaluationToDelete = evaluationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        );
        evaluationRepository.delete(evaluationToDelete);
        return new ApiResponse("Evaluation deleted successfully", HttpStatus.OK, null);
    }
}

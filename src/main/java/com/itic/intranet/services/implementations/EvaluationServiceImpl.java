package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.services.EvaluationService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllEvaluations() {
        List<Evaluation> allEvaluations = evaluationRepository.findAll();
        return allEvaluations.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("No evaluations found", allEvaluations))
                : ResponseEntity.ok(new ApiResponse("List of evaluations", allEvaluations));
    }

    @Override
    public ResponseEntity<ApiResponse> getEvaluationById(Long id) {
        return evaluationRepository.findById(id)
                .map(evaluation -> ResponseEntity.ok(new ApiResponse("Evaluation found", evaluation)))
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> getEvaluationByTitle(String title) {
        List<Evaluation> evaluations = evaluationRepository.findByTitleContaining(title);
        if (evaluations.isEmpty()) {
            throw new BadRequestException("No evaluations found for title: " + title);
        }
        return ResponseEntity.ok(new ApiResponse("List of evaluations", evaluations));
    }

    @Override
    public ResponseEntity<ApiResponse> addEvaluation(EvaluationRequestDto evaluationDto) {
        if (evaluationDto.getTitle() == null || evaluationDto.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Evaluation title is required");
        }

        Evaluation newEvaluation = Evaluation.builder()
                .title(evaluationDto.getTitle())
                .build();
        Evaluation savedEvaluation = evaluationRepository.save(newEvaluation);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Evaluation created successfully", savedEvaluation));
    }

    @Override
    public ResponseEntity<ApiResponse> updateEvaluation(Long id, EvaluationRequestDto evaluationDto) {
        if (evaluationDto.getTitle() == null || evaluationDto.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Evaluation title is required");
        }
        return evaluationRepository.findById(id)
                .map(existingEvaluation -> {
                    existingEvaluation.setTitle(evaluationDto.getTitle());
                    Evaluation updatedEvaluation = evaluationRepository.save(existingEvaluation);
                    return ResponseEntity.ok(new ApiResponse("Evaluation updated successfully", updatedEvaluation));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteEvaluation(Long id) {
        return evaluationRepository.findById(id)
                .map(evaluation -> {
                    evaluationRepository.delete(evaluation);
                    return ResponseEntity.ok(new ApiResponse("Evaluation deleted successfully", null));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with ID: " + id));
    }
}

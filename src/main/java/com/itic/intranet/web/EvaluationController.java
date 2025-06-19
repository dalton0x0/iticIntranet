package com.itic.intranet.web;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.services.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v18/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/all")
    public ResponseEntity<List<EvaluationDetailedResponseDto>> getAllEvaluations() {
        List<EvaluationDetailedResponseDto> evaluations = evaluationService.getAllEvaluations();
        return evaluations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDto> getEvaluationById(@PathVariable Long id) {
        EvaluationResponseDto evaluation = evaluationService.getEvaluationById(id);
        return ResponseEntity.ok(evaluation);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EvaluationResponseDto>> searchEvaluation(@RequestParam String title) {
        List<EvaluationResponseDto> evaluations = evaluationService.searchEvaluations(title);
        return evaluations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(evaluations);
    }

    @PostMapping("/teacher/{teacherId}/add-evaluation")
    public ResponseEntity<EvaluationResponseDto> createEvaluation(@RequestBody EvaluationRequestDto evaluationDto, @PathVariable Long teacherId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.createEvaluation(evaluationDto, teacherId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationResponseDto> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationRequestDto evaluationDto) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.updateEvaluation(id, evaluationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finishEvaluation(@PathVariable Long id) {
        evaluationService.finishEvaluation(id);
        return ResponseEntity.ok().build();
    }
}

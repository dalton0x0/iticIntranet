package com.itic.intranet.web;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.services.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v7/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/all")
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return evaluations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationDetailedResponseDto> getEvaluationById(@PathVariable Long id) {
        EvaluationDetailedResponseDto evaluation = evaluationService.getEvaluationById(id);
        return evaluation == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(evaluation);

    }

    @PostMapping("/add/teacher/{teacherId}")
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody EvaluationRequestDto evaluationDto, @PathVariable Long teacherId) {
        Evaluation evaluation = evaluationService.createEvaluation(evaluationDto, teacherId);
        return evaluation == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(evaluation);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationRequestDto evaluationDto) {
        Evaluation evaluation = evaluationService.updateEvaluation(id, evaluationDto);
        return evaluation == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(evaluation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{evaluationId}/classrooms/add/{classroomId}")
    public ResponseEntity<Evaluation> addClassroomToEvaluation(@PathVariable Long evaluationId, @PathVariable Long classroomId) {
        return ResponseEntity.ok(evaluationService.addClassroomToEvaluation(evaluationId, classroomId));
    }
}
package com.itic.intranet.web;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.services.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v18/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping
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

    @PostMapping("/add/teacher/{teacherId}")
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

    @PostMapping("/{evaluationId}/classrooms/add/{classroomId}")
    public ResponseEntity<Void> addClassroomToEvaluation(@PathVariable Long evaluationId, @PathVariable Long classroomId) {
        evaluationService.addClassroomToEvaluation(evaluationId, classroomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{evaluationId}/classrooms/remove/{classroomId}")
    public ResponseEntity<Void> removeClassroomToEvaluation(@PathVariable Long evaluationId, @PathVariable Long classroomId) {
        evaluationService.removeClassroomToEvaluation(evaluationId, classroomId);
        return ResponseEntity.ok().build();
    }
}

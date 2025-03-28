package com.itic.intranet.web;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.services.EvaluationService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEvaluationById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getEvaluationById(id));
    }

    @PostMapping("/add/teacher/{teacherId}")
    public ResponseEntity<ApiResponse> createEvaluation(@RequestBody EvaluationRequestDto evaluationDto, @PathVariable Long teacherId) {
        ApiResponse response = evaluationService.createEvaluation(evaluationDto, teacherId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationRequestDto evaluationDto) {
        return ResponseEntity.ok(evaluationService.updateEvaluation(id, evaluationDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEvaluation(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.deleteEvaluation(id));
    }

    @PostMapping("/{evaluationId}/classrooms/add/{classroomId}")
    public ResponseEntity<ApiResponse> addClassroomToEvaluation(@PathVariable Long evaluationId, @PathVariable Long classroomId) {
        return ResponseEntity.ok(evaluationService.addClassroomToEvaluation(evaluationId, classroomId));
    }

    @GetMapping("/{evaluationId}/results")
    public ResponseEntity<ApiResponse> getEvaluationResults(@PathVariable Long evaluationId) {
        return ResponseEntity.ok(evaluationService.getEvaluationResults(evaluationId));
    }
}
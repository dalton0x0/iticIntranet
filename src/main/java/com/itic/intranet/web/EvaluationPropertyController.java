package com.itic.intranet.web;

import com.itic.intranet.services.EvaluationPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v18/evaluations/{evaluationId}")
@RequiredArgsConstructor
public class EvaluationPropertyController {

    private final EvaluationPropertyService evaluationPropertyService;

    @PutMapping("/add-classroom/{classroomId}")
    public ResponseEntity<Void> addClassroomToEvaluation(@PathVariable Long evaluationId, @PathVariable Long classroomId) {
        evaluationPropertyService.addClassroomToEvaluation(evaluationId, classroomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-classroom/{classroomId}")
    public ResponseEntity<Void> removeClassroomToEvaluation(@PathVariable Long evaluationId, @PathVariable Long classroomId) {
        evaluationPropertyService.removeClassroomToEvaluation(evaluationId, classroomId);
        return ResponseEntity.ok().build();
    }
}

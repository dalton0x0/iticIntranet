package com.itic.intranet.web;

import com.itic.intranet.services.EvaluationPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v18/evaluation/{evaluationId}")
public class EvaluationPropertyController {

    @Autowired
    private EvaluationPropertyService evaluationPropertyService;

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

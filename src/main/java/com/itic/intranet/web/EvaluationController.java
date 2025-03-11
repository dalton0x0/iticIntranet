package com.itic.intranet.web;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.services.EvaluationService;
import com.itic.intranet.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/evaluation")
@CrossOrigin(origins = "http://localhost:63343")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/test")
    public ApiResponse test() {
        return new ApiResponse("API TEST OK !", HttpStatus.OK, null);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getEvaluationById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getEvaluationById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getEvaluationByTitle(@RequestParam(required = false) String title) {
        return ResponseEntity.ok(evaluationService.getEvaluationByTitle(title));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addEvaluation(@Valid @RequestBody EvaluationRequestDto evaluationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.addEvaluation(evaluationDto));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateEvaluation(@PathVariable Long id,@Valid @RequestBody EvaluationRequestDto evaluationDto) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.updateEvaluation(id, evaluationDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteEvaluation(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.deleteEvaluation(id));
    }
} 

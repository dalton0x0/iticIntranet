package com.itic.intranet.web;

import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.services.EvaluationService;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/evaluation")
@CrossOrigin(origins = "http://localhost:63343")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(new ApiResponse("API TEST OK !", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getEvaluationById(@PathVariable Long id) {
        return evaluationService.getEvaluationById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getEvaluationByTitle(@RequestParam(required = false) String title) {
        return evaluationService.getEvaluationByTitle(title);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addEvaluation(@RequestBody EvaluationRequestDto evaluationDto) {
        return evaluationService.addEvaluation(evaluationDto);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationRequestDto evaluationDto) {
        return evaluationService.updateEvaluation(id, evaluationDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteEvaluation(@PathVariable Long id) {
        return evaluationService.deleteEvaluation(id);
    }
} 

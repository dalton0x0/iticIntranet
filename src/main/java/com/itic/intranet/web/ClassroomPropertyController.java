package com.itic.intranet.web;

import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.services.ClassroomPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v18/classrooms/{classroomId}")
@RequiredArgsConstructor
public class ClassroomPropertyController {

    private final ClassroomPropertyService classroomPropertyService;

    @GetMapping("/teachers")
    public ResponseEntity<List<UserMinimalDto>> getTeachersOfClassroom(@PathVariable Long classroomId) {
        List<UserMinimalDto> teachers = classroomPropertyService.getClassroomTeachers(classroomId);
        return teachers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teachers);
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserMinimalDto>> getStudentsOfClassroom(@PathVariable Long classroomId) {
        List<UserMinimalDto> students = classroomPropertyService.getClassroomStudents(classroomId);
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/evaluations")
    public ResponseEntity<List<EvaluationResponseDto>> getEvaluationsOfClassroom(@PathVariable Long classroomId) {
        List<EvaluationResponseDto> evaluations = classroomPropertyService.getClassroomEvaluations(classroomId);
        return evaluations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(evaluations);
    }
}

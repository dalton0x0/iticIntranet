package com.itic.intranet.web;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.services.ClassroomService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/classroom")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getClassroomById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createClassroom(@RequestBody ClassroomRequestDto classroomDto) {
        ApiResponse response = classroomService.createClassroom(classroomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long id, @RequestBody ClassroomRequestDto classroomDto) {
        return ResponseEntity.ok(classroomService.updateClassroom(id, classroomDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteClassroom(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.deleteClassroom(id));
    }

    @PostMapping("/{classroomId}/students/add/{studentId}")
    public ResponseEntity<ApiResponse> addStudent(@PathVariable Long classroomId, @PathVariable Long studentId) {
        return ResponseEntity.ok(classroomService.addStudentToClassroom(classroomId, studentId));
    }

    @DeleteMapping("/{classroomId}/students/remove/{studentId}")
    public ResponseEntity<ApiResponse> removeStudent(@PathVariable Long classroomId, @PathVariable Long studentId) {
        return ResponseEntity.ok(classroomService.removeStudentFromClassroom(classroomId, studentId));
    }

    @GetMapping("/{classroomId}/students")
    public ResponseEntity<ApiResponse> getClassroomStudents(@PathVariable Long classroomId) {
        return ResponseEntity.ok(classroomService.getClassroomStudents(classroomId));
    }

    @GetMapping("/{classroomId}/teachers")
    public ResponseEntity<ApiResponse> getClassroomTeachers(@PathVariable Long classroomId) {
        return ResponseEntity.ok(classroomService.getClassroomTeachers(classroomId));
    }
}
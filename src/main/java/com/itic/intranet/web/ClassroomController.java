package com.itic.intranet.web;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.ClassroomResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v18/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDto>> getAllClassrooms() {
        List<ClassroomResponseDto> classrooms = classroomService.getAllClassrooms();
        return classrooms.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDto> getClassroomById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClassroomResponseDto>> searchClassroom(@RequestParam String keyword) {
        List<ClassroomResponseDto> classrooms = classroomService.searchClassroom(keyword);
        return classrooms.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(classrooms);
    }

    @PostMapping
    public ResponseEntity<ClassroomResponseDto> createClassroom(@RequestBody ClassroomRequestDto classroomDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classroomService.createClassroom(classroomDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDto> updateClassroom(@PathVariable Long id, @RequestBody ClassroomRequestDto classroomDto) {
        return ResponseEntity.status(HttpStatus.OK).body(classroomService.updateClassroom(id, classroomDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.ok().build();
    }
}

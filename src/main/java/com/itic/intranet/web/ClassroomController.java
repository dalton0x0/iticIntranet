package com.itic.intranet.web;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v9/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/all")
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        return classrooms.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable Long id) {
        Classroom classroom = classroomService.getClassroomById(id);
        return ResponseEntity.ok(classroom);
    }

    @PostMapping("/add")
    public ResponseEntity<Classroom> createClassroom(@RequestBody ClassroomRequestDto classroomDto) {
        Classroom classroom = classroomService.createClassroom(classroomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(classroom);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Classroom> updateClassroom(@PathVariable Long id, @RequestBody ClassroomRequestDto classroomDto) {
        Classroom classroom = classroomService.updateClassroom(id, classroomDto);
        return ResponseEntity.ok(classroom);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{classroomId}/user/add/{userId}")
    public ResponseEntity<Void> addUserToClassroom(@PathVariable Long classroomId, @PathVariable Long userId) {
        classroomService.addUserToClassroom(classroomId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{classroomId}/user/remove/{userId}")
    public ResponseEntity<Void> removeUserFromClassroom(@PathVariable Long classroomId, @PathVariable Long userId) {
        classroomService.removeUserFromClassroom(classroomId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{classroomId}/student/all")
    public ResponseEntity<List<UserMinimalDto>> getClassroomStudents (@PathVariable Long classroomId) {
        List<UserMinimalDto> students = classroomService.getClassroomStudents(classroomId);
        return students.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/{classroomId}/teacher/all")
    public ResponseEntity<List<UserMinimalDto>> getClassroomTeachers (@PathVariable Long classroomId) {
        List<UserMinimalDto> teachers = classroomService.getClassroomTeachers(classroomId);
        return teachers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teachers);
    }
}
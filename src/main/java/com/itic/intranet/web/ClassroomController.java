package com.itic.intranet.web;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.services.ClassroomService;
import com.itic.intranet.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/classroom")
@CrossOrigin(origins = "http://localhost:63343")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/test")
    public ApiResponse test() {
        return new ApiResponse("API TEST OK !", HttpStatus.OK, null);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getClassroomById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getClassroomByName(@RequestParam String name) {
        return ResponseEntity.ok(classroomService.getClassroomByName(name));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addClassroom(@Valid @RequestBody ClassroomRequestDto classroomDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(classroomService.addClassroom(classroomDto));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long id,@Valid @RequestBody ClassroomRequestDto classroomDto) {
        return ResponseEntity.status(HttpStatus.OK).body(classroomService.updateClassroom(id, classroomDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteClassroom(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(classroomService.deleteClassroom(id));
    }
}

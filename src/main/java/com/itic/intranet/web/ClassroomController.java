package com.itic.intranet.web;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.services.ClassroomService;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/classroom")
@CrossOrigin(origins = "http://localhost:63343")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(new ApiResponse("API TEST OK !", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllClassrooms() {
        return classroomService.getAllClassrooms();
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getClassroomById(@PathVariable Long id) {
        return classroomService.getClassroomById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getClassroomByName(@RequestParam String name) {
        return classroomService.getClassroomByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addClassroom(@RequestBody ClassroomRequestDto classroomDto) {
        return classroomService.addClassroom(classroomDto);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateClassroom(@PathVariable Long id, @RequestBody ClassroomRequestDto classroomDto) {
        return classroomService.updateClassroom(id, classroomDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteClassroom(@PathVariable Long id) {
        return classroomService.deleteClassroom(id);
    }
}

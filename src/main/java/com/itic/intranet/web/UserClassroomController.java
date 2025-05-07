package com.itic.intranet.web;

import com.itic.intranet.services.UserClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v18/users/{userId}/classroom")
public class UserClassroomController {

    @Autowired
    private UserClassroomService userClassroomService;

    @PutMapping("/{classroomId}")
    public ResponseEntity<Void> assignUserToClassroom(@PathVariable Long userId, @PathVariable Long classroomId) {
        userClassroomService.assignClassroomToUser(userId, classroomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{classroomId}")
    public ResponseEntity<Void> removeUserToClassroom(@PathVariable Long userId, @PathVariable Long classroomId) {
        userClassroomService.removeClassroomFromUser(userId, classroomId);
        return ResponseEntity.ok().build();
    }
}

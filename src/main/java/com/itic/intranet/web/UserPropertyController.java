package com.itic.intranet.web;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.services.UserPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v18/users/{userId}")
public class UserPropertyController {

    @Autowired
    private UserPropertyService userPropertyService;

    @PutMapping("/role/add/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userPropertyService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/role/remove/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId) {
        userPropertyService.removeRoleFromUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role")
    public ResponseEntity<RoleType> getUserRoleType(@PathVariable Long userId) {
        return ResponseEntity.ok(userPropertyService.getUserRoleType(userId));
    }

    @PutMapping("/classroom/add/{classroomId}")
    public ResponseEntity<Void> assignUserToClassroom(@PathVariable Long userId, @PathVariable Long classroomId) {
        userPropertyService.assignClassroomToUser(userId, classroomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/classroom/remove/{classroomId}")
    public ResponseEntity<Void> removeUserToClassroom(@PathVariable Long userId, @PathVariable Long classroomId) {
        userPropertyService.removeClassroomFromUser(userId, classroomId);
        return ResponseEntity.ok().build();
    }
}

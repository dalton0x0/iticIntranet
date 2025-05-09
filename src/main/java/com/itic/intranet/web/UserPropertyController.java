package com.itic.intranet.web;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.services.UserPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v18/users/{userId}")
@RequiredArgsConstructor
public class UserPropertyController {

    private final UserPropertyService userPropertyService;

    @PutMapping("/add-role/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userPropertyService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-role/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userPropertyService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/role")
    public ResponseEntity<RoleType> getRoleOfUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userPropertyService.getRoleOfUser(userId));
    }

    @PutMapping("/add-classroom/{classroomId}")
    public ResponseEntity<Void> assignUserToClassroom(@PathVariable Long userId, @PathVariable Long classroomId) {
        userPropertyService.assignClassroomToUser(userId, classroomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-classroom/{classroomId}")
    public ResponseEntity<Void> removeUserToClassroom(@PathVariable Long userId, @PathVariable Long classroomId) {
        userPropertyService.removeClassroomFromUser(userId, classroomId);
        return ResponseEntity.ok().build();
    }
}

package com.itic.intranet.web;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v18/users/{userId}/role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PutMapping("/{roleId}")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userRoleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long userId) {
        userRoleService.removeRoleFromUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<RoleType> getUserRoleType(@PathVariable Long userId) {
        return ResponseEntity.ok(userRoleService.getUserRoleType(userId));
    }
}

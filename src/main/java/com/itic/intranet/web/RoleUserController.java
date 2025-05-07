package com.itic.intranet.web;

import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.services.RoleUserService;
import com.itic.intranet.services.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v18/roles/{userId}/users")
public class RoleUserController {

    @Autowired
    private RoleUserService  roleUserService ;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUserRoleType(@PathVariable Long userId) {
        List<UserResponseDto> users = roleUserService.getUsersByRole(userId);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }
}

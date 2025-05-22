package com.itic.intranet.web;

import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.services.RolePropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v18/roles/{roleId}")
@RequiredArgsConstructor
public class RolePropertyController {

    private final RolePropertyService rolePropertyService ;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUsersOfRole(@PathVariable Long roleId) {
        List<UserResponseDto> users = rolePropertyService.getRoleUsers(roleId);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }
}

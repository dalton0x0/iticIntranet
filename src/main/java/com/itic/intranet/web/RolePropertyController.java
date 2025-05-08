package com.itic.intranet.web;

import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.services.RolePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v18/roles/{roleId}")
public class RolePropertyController {

    @Autowired
    private RolePropertyService rolePropertyService ;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUsersOfRole(@PathVariable Long roleId) {
        List<UserResponseDto> users = rolePropertyService.getUsersOfRole(roleId);
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }
}

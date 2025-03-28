package com.itic.intranet.web;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.services.RoleService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v7/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createRole(@RequestBody RoleRequestDto roleDto) {
        ApiResponse response = roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id, @RequestBody RoleRequestDto roleDto) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
}
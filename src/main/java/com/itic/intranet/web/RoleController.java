package com.itic.intranet.web;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.models.Role;
import com.itic.intranet.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v9/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return roles.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Role> createRole(@RequestBody RoleRequestDto roleDto) {
        Role role = roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody RoleRequestDto roleDto) {
        Role updatedRole = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }
}
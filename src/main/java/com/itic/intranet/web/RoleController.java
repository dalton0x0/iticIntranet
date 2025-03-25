package com.itic.intranet.web;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.models.Role;
import com.itic.intranet.services.RoleService;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/role")
@CrossOrigin(origins = "http://localhost:63343")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test() {
        return ResponseEntity.ok(new ApiResponse("API TEST OK !", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return roles.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("List of roles is empty", roles))
                : ResponseEntity.ok(new ApiResponse("List of roles", roles));
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(new ApiResponse("Role found", role));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRole(@RequestBody RoleRequestDto roleDto) {
        Role savedRole = roleService.addRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Role created successfully", savedRole));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getRoleByWording(@RequestParam String wording) {
        Role role = roleService.getRoleByWording(wording);
        return ResponseEntity.ok(new ApiResponse("Role found", role));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id, @RequestBody RoleRequestDto roleDto) {
        Role updatedRole = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(new ApiResponse("Role updated successfully", updatedRole));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(new ApiResponse("Role deleted successfully", null));
    }
}

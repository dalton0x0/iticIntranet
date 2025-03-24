package com.itic.intranet.web;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.services.RoleService;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse> getALllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRole(@RequestBody RoleRequestDto roleDto) {
        return roleService.addRole(roleDto);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id, @RequestBody RoleRequestDto roleDto) {
        return roleService.updateRole(id, roleDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}

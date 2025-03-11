package com.itic.intranet.web;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.services.RoleService;
import com.itic.intranet.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/role")
@CrossOrigin(origins = "http://localhost:63343")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/test")
    public ApiResponse test() {
        return new ApiResponse("API TEST OK !", HttpStatus.OK, null);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getALllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRole(@Valid @RequestBody RoleRequestDto roleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addRole(roleDto));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id,@Valid @RequestBody RoleRequestDto roleDto) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.updateRole(id, roleDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.deleteRole(id));
    }
}

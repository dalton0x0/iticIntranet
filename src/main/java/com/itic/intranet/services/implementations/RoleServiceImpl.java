package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Role;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.services.RoleService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllRoles() {
        List<Role> allRoles = roleRepository.findAll();
        return allRoles.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("List of roles is empty", allRoles))
                : ResponseEntity.ok(new ApiResponse("List of roles", allRoles));
    }

    @Override
    public ResponseEntity<ApiResponse> getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return ResponseEntity.ok(new ApiResponse("Role found", role));
    }

    @Override
    public ResponseEntity<ApiResponse> addRole(RoleRequestDto roleDto) {
        validateRoleDto(roleDto);
        Role newRole = Role.builder()
                .wording(roleDto.getWording().trim())
                .build();
        Role savedRole = roleRepository.save(newRole);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Role created successfully", savedRole));
    }

    @Override
    public ResponseEntity<ApiResponse> updateRole(Long id, RoleRequestDto roleDto) {
        validateRoleDto(roleDto);
        Role updatedRole = roleRepository.findById(id)
                .map(role -> {
                    role.setWording(roleDto.getWording().trim());
                    return roleRepository.save(role);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return ResponseEntity.ok(new ApiResponse("Role updated successfully", updatedRole));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteRole(Long id) {
        Role roleToDelete = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        roleRepository.delete(roleToDelete);
        return ResponseEntity.ok(new ApiResponse("Role deleted successfully", null));
    }

    private void validateRoleDto(RoleRequestDto roleDto) {
        if (roleDto.getWording() == null || roleDto.getWording().trim().isEmpty()) {
            throw new BadRequestException("Role wording cannot be empty");
        }
    }
}

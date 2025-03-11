package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Role;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.services.RoleService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public ApiResponse getAllRoles() {
        List<Role> allRoles = roleRepository.findAll();
        if (allRoles.isEmpty()) {
            return new ApiResponse("List of roles is empty", HttpStatus.NO_CONTENT, allRoles);
        }
        return new ApiResponse("List of roles", HttpStatus.OK, allRoles);
    }

    @Override
    public ApiResponse getRoleById(Long id) {
        Optional<Role> role = Optional.ofNullable(roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found")
        ));
        return new ApiResponse("Role found", HttpStatus.OK, role);
    }

    @Override
    public ApiResponse addRole(RoleRequestDto roleDto) {
        var newRole = Role.builder()
                .wording(roleDto.getWording())
                .build();
        Role savedRole = roleRepository.save(newRole);
        return new ApiResponse("Role created successfully", HttpStatus.CREATED, savedRole);
    }

    @Override
    public ApiResponse updateRole(Long id, RoleRequestDto roleDto) {
        Role existingRole = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found")
        );
        existingRole.setWording(roleDto.getWording());
        Role updatedRole = roleRepository.save(existingRole);
        return new ApiResponse("Role updated successfully", HttpStatus.OK, updatedRole);
    }

    @Override
    public ApiResponse deleteRole(Long id) {
        Role roleToDelete = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found")
        );
        roleRepository.delete(roleToDelete);
        return new ApiResponse("Role deleted successfully", HttpStatus.OK, null);
    }
}

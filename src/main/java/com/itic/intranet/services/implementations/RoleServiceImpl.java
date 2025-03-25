package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Role;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public Role getRoleByWording(String wording) {
        return roleRepository.findByWording(wording)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public Role addRole(RoleRequestDto roleDto) {
        validateRoleDto(roleDto);
        Role newRole = Role.builder()
                .wording(roleDto.getWording().trim())
                .build();
        return roleRepository.save(newRole);
    }

    @Override
    public Role updateRole(Long id, RoleRequestDto roleDto) {
        validateRoleDto(roleDto);
        return roleRepository.findById(id)
                .map(role -> {
                    role.setWording(roleDto.getWording().trim());
                    return roleRepository.save(role);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public void deleteRole(Long id) {
        Role roleToDelete = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        roleRepository.delete(roleToDelete);
    }

    private void validateRoleDto(RoleRequestDto roleDto) {
        if (roleDto.getWording() == null || roleDto.getWording().trim().isEmpty()) {
            throw new BadRequestException("Role wording cannot be empty");
        }
    }
}

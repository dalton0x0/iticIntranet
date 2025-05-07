package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.dtos.RoleResponseDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.mappers.RoleMapper;
import com.itic.intranet.models.Role;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return roleMapper.convertEntityToResponseDto(role);
    }

    @Override
    public List<RoleResponseDto> searchRole(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        return roleRepository.findByLabel(label)
                .stream()
                .map(roleMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto roleDto) {
        validateRoleRequest(roleDto);
        checkUniqueConstraints(roleDto);
        Role role = roleMapper.convertToDtoEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.convertEntityToResponseDto(savedRole);
    }

    @Override
    public RoleResponseDto updateRole(Long id, RoleRequestDto roleDto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        validateRoleRequest(roleDto);
        checkUniqueConstraintsForUpdate(id, roleDto);

        roleMapper.updateEntityFromDto(roleDto, existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.convertEntityToResponseDto(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }

        long userCount = userRepository.countByRole_Id(id);
        if (userCount > 0) {
            throw new BadRequestException("Unable to delete this role . There is one or more users with this role : " + userCount);
        }

        roleRepository.deleteById(id);
    }

    private void validateRoleRequest(RoleRequestDto roleDto) {
        if (roleDto.getRoleType() == null) {
            throw new BadRequestException("Role type is required");
        }
        if (roleDto.getLabel() == null || roleDto.getLabel().trim().isEmpty()) {
            throw new BadRequestException("Label is required");
        }
        if (roleDto.getLabel().length() > 20) {
            throw new BadRequestException("The label must not exceed 20 characters");
        }
    }

    private void checkUniqueConstraints(RoleRequestDto dto) {
        roleRepository.findByRoleType(dto.getRoleType()).ifPresent(role -> {
            throw new BadRequestException("This role type already exists");
        });

        roleRepository.findByLabel(dto.getLabel()).ifPresent(role -> {
            throw new BadRequestException("This label already exists");
        });
    }

    public void checkUniqueConstraintsForUpdate(Long roleId, RoleRequestDto roleDto) {
        Optional<Role> existingRoleType = roleRepository.findByRoleType(roleDto.getRoleType());
        if (existingRoleType.isPresent() && !existingRoleType.get().getId().equals(roleId)) {
            throw new BadRequestException("A role with this type already exists");
        }
        if (existingRoleType.isPresent() && existingRoleType != roleRepository.findByRoleType(roleDto.getRoleType())) {
            throw new BadRequestException("The role type cannot be changed");
        }
        Optional<Role> existingLabel = roleRepository.findByLabel(roleDto.getLabel());
        if (existingLabel.isPresent() && !existingLabel.get().getId().equals(roleId)) {
            throw new BadRequestException("A role with this label already exists");
        }
    }
}

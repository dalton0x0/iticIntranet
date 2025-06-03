package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.dtos.RoleResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.RoleMapper;
import com.itic.intranet.models.mysql.Role;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.LogService;
import com.itic.intranet.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;
    private final EntityHelper entityHelper;
    private final LogService logService;

    @Override
    public List<RoleResponseDto> getAllRoles() {
        List<RoleResponseDto> allRoles = roleRepository.findAll()
                .stream()
                .map(roleMapper::convertEntityToResponseDto)
                .toList();
        logService.info(
                "SYSTEM",
                "GET_ALL_ROLES",
                "Getting all roles",
                Map.of(
                        "resultCount", allRoles.size()
                )
        );
        return allRoles;
    }

    @Override
    public RoleResponseDto getRoleById(Long id) {
        Role role = entityHelper.getRole(id);
        logService.info(
                "SYSTEM",
                "GET_ROLE",
                "Getting role by ID",
                Map.of(
                        "roleId", role.getId(),
                        "resultFound", role.getRoleType()
                )
        );
        return roleMapper.convertEntityToResponseDto(role);
    }

    @Override
    public List<RoleResponseDto> searchRole(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        List<RoleResponseDto> results = roleRepository.findByLabel(label)
                .stream()
                .map(roleMapper::convertEntityToResponseDto)
                .toList();
        logService.info(
                "SYSTEM",
                "SEARCH_ROLE",
                "Searching roles",
                Map.of(
                        "keyword", label,
                        "resultCount", results.size()
                )
        );
        return results;
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto roleDto) {
        validateRoleRequest(roleDto);
        checkUniqueConstraints(roleDto);
        Role role = roleMapper.convertToDtoEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        logService.info(
                "SYSTEM",
                "CREATE_ROLE",
                "Creating new role",
                Map.of(
                        "roleId", savedRole.getId(),
                        "roleCreated", savedRole.getRoleType()
                )
        );
        return roleMapper.convertEntityToResponseDto(savedRole);
    }

    @Override
    public RoleResponseDto updateRole(Long id, RoleRequestDto roleDto) {
        Role existingRole = entityHelper.getRole(id);
        validateRoleRequest(roleDto);
        checkUniqueConstraintsForUpdate(id, roleDto);
        roleMapper.updateEntityFromDto(roleDto, existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        logService.info(
                "SYSTEM",
                "UPDATE_ROLE",
                "Updating new role",
                Map.of(
                        "roleId", updatedRole.getId(),
                        "roleUpdated", updatedRole.getRoleType()
                )
        );
        return roleMapper.convertEntityToResponseDto(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = entityHelper.getRole(id);
        long userCount = userRepository.countByRole_Id(id);
        if (userCount > 0) {
            throw new BadRequestException("Unable to delete this role . There is one or more users with this role : " + userCount);
        }
        roleRepository.delete(role);
        logService.info(
                "SYSTEM",
                "DELETE_ROLE",
                "Deleting new role",
                Map.of(
                        "roleId", role.getId(),
                        "roleDeleted", role.getRoleType()
                )
        );
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

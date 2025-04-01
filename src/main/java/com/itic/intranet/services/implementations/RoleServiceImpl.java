package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.dtos.RoleResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Role;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé avec l'ID : " + id));
    }

    @Override
    public Role createRole(RoleRequestDto roleDto) {
        validateRoleRequest(roleDto);
        checkUniqueConstraints(roleDto);
        var role = Role.builder()
                .roleType(roleDto.getRoleType())
                .wording(roleDto.getWording())
                .build();
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, RoleRequestDto roleDto) {
        Role role = getRoleById(id);
        validateRoleRequest(roleDto);

        if (!role.getRoleType().equals(roleDto.getRoleType())) {
            throw new BadRequestException("Le type de rôle ne peut pas être modifié");
        }

        role.setWording(roleDto.getWording());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = getRoleById(id);

        long userCount = userRepository.countByRole_Id(id);
        if (userCount > 0) {
            throw new BadRequestException("Impossible de supprimer : " + userCount + " utilisateur(s) ont ce rôle");
        }

        roleRepository.delete(role);
    }

    private void validateRoleRequest(RoleRequestDto dto) {
        if (dto.getRoleType() == null) {
            throw new BadRequestException("Le type de rôle est obligatoire");
        }
        if (dto.getWording() == null || dto.getWording().trim().isEmpty()) {
            throw new BadRequestException("Le libellé est obligatoire");
        }
        if (dto.getWording().length() > 50) {
            throw new BadRequestException("Le libellé ne doit pas dépasser 50 caractères");
        }
    }

    private void checkUniqueConstraints(RoleRequestDto dto) {
        roleRepository.findByRoleType(dto.getRoleType()).ifPresent(role -> {
            throw new BadRequestException("Un rôle avec ce type existe déjà");
        });

        roleRepository.findByWording(dto.getWording()).ifPresent(r -> {
            throw new BadRequestException("Un rôle avec ce libellé existe déjà");
        });
    }

    private RoleResponseDto convertToDto(Role role) {
        return RoleResponseDto.builder()
                .id(role.getId())
                .roleType(role.getRoleType())
                .wording(role.getWording())
                .build();
    }
}
package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.dtos.RoleResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Role;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.RoleService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun rôle trouvé");
        }

        List<RoleResponseDto> response = roles.stream()
                .map(this::convertToDto)
                .toList();

        return ApiResponse.builder()
                .message(roles.size() + " rôles trouvés")
                .response(response)
                .build();
    }

    @Override
    public ApiResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé avec l'ID : " + id));

        return ApiResponse.builder()
                .message("Rôle trouvé")
                .response(convertToDto(role))
                .build();
    }

    @Override
    public ApiResponse createRole(RoleRequestDto roleDto) {
        validateRoleRequest(roleDto);
        checkUniqueConstraints(roleDto);

        Role role = new Role();
        role.setRoleType(roleDto.getRoleType());
        role.setWording(roleDto.getWording());

        Role savedRole = roleRepository.save(role);
        return ApiResponse.builder()
                .message("Rôle créé avec succès")
                .response(convertToDto(savedRole))
                .build();
    }

    @Override
    public ApiResponse updateRole(Long id, RoleRequestDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé avec l'ID : " + id));

        validateRoleRequest(roleDto);

        if (!role.getRoleType().equals(roleDto.getRoleType())) {
            throw new BadRequestException("Le type de rôle ne peut pas être modifié");
        }

        role.setWording(roleDto.getWording());
        Role updatedRole = roleRepository.save(role);

        return ApiResponse.builder()
                .message("Rôle mis à jour avec succès")
                .response(convertToDto(updatedRole))
                .build();
    }

    @Override
    public ApiResponse deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé avec l'ID : " + id));

        long userCount = userRepository.countByRoleId(id);
        if (userCount > 0) {
            throw new BadRequestException("Impossible de supprimer : " + userCount + " utilisateur(s) ont ce rôle");
        }

        roleRepository.delete(role);
        return ApiResponse.builder()
                .message("Rôle supprimé avec succès")
                .build();
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
        roleRepository.findByRoleType(dto.getRoleType()).ifPresent(r -> {
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
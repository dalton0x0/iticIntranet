package com.itic.intranet.mappers;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.dtos.RoleResponseDto;
import com.itic.intranet.models.mysql.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public Role convertToDtoEntity(RoleRequestDto roleDto) {
        return Role.builder()
                .id(roleDto.getId())
                .roleType(roleDto.getRoleType())
                .label(roleDto.getLabel().trim())
                .build();
    }

    public RoleResponseDto convertEntityToResponseDto(Role role) {
        return RoleResponseDto.builder()
                .id(role.getId())
                .roleType(role.getRoleType())
                .label(role.getLabel())
                .build();
    }

    public void updateEntityFromDto(RoleRequestDto roleRequestDto, Role role) {
        role.setRoleType(roleRequestDto.getRoleType());
        role.setLabel(roleRequestDto.getLabel().trim());
    }
}

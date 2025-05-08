package com.itic.intranet.services;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.dtos.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto getRoleById(Long id);
    List<RoleResponseDto> searchRole(String label);
    RoleResponseDto createRole(RoleRequestDto roleDto);
    RoleResponseDto updateRole(Long id, RoleRequestDto roleDto);
    void deleteRole(Long id);
}

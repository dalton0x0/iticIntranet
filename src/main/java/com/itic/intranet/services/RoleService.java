package com.itic.intranet.services;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface RoleService {
    ApiResponse getAllRoles();
    ApiResponse getRoleById(Long id);
    ApiResponse createRole(RoleRequestDto roleDto);
    ApiResponse updateRole(Long id, RoleRequestDto roleDto);
    ApiResponse deleteRole(Long id);
}
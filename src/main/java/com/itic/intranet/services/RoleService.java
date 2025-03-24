package com.itic.intranet.services;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    ResponseEntity<ApiResponse> getAllRoles();
    ResponseEntity<ApiResponse> getRoleById(Long id);
    ResponseEntity<ApiResponse> addRole(RoleRequestDto roleDto);
    ResponseEntity<ApiResponse> updateRole(Long id, RoleRequestDto roleDto);
    ResponseEntity<ApiResponse> deleteRole(Long id);
}

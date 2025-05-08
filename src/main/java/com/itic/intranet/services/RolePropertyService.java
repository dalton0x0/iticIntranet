package com.itic.intranet.services;

import com.itic.intranet.dtos.UserResponseDto;

import java.util.List;

public interface RolePropertyService {
    List<UserResponseDto> getUsersByRole(Long roleId);
}

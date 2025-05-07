package com.itic.intranet.services;

import com.itic.intranet.dtos.UserResponseDto;

import java.util.List;

public interface RoleUserService {
    List<UserResponseDto> getUsersByRole(Long roleId);
}

package com.itic.intranet.services;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface UserService {
    ApiResponse getAllUsers();
    ApiResponse getAllActiveUsers();
    ApiResponse getUserById(Long id);
    ApiResponse searchUsers(String keyword);
    ApiResponse createUser(UserRequestDto userDto);
    ApiResponse updateUser(Long id, UserRequestDto userDto);
    ApiResponse deactivateUser(Long id);
    ApiResponse permanentlyDeleteUser(Long id);
}
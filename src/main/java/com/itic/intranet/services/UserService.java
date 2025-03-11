package com.itic.intranet.services;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface UserService {
    ApiResponse getAllUsers();
    ApiResponse getAllActiveUsers();
    ApiResponse getUserById(Long id);
    ApiResponse findByFirstnameOrLastname(String firstname ,String lastName);
    ApiResponse addUser(UserRequestDto userDto);
    ApiResponse updateUser(Long id, UserRequestDto userDto);
    ApiResponse deleteUser(Long id);
    ApiResponse removeUser(Long id);
}

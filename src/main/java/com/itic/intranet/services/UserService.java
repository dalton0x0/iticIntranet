package com.itic.intranet.services;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();
    List<UserResponseDto> getAllActiveUsers();
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> searchUser(String keyword);
    UserResponseDto createUser(UserRequestDto userDto);
    UserResponseDto updateUser(Long id, UserRequestDto userDto);
    void deactivateUser(Long id);
    void permanentlyDeleteUser(Long id);
}

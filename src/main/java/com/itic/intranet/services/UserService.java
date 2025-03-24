package com.itic.intranet.services;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse> getAllUsers();
    ResponseEntity<ApiResponse> getAllActiveUsers();
    ResponseEntity<ApiResponse> getUserById(Long id);
    ResponseEntity<ApiResponse> findByFirstnameOrLastname(String firstname , String lastName);
    ResponseEntity<ApiResponse> addUser(UserRequestDto userDto);
    ResponseEntity<ApiResponse> updateUser(Long id, UserRequestDto userDto);
    ResponseEntity<ApiResponse> deleteUser(Long id);
    ResponseEntity<ApiResponse> removeUser(Long id);
}

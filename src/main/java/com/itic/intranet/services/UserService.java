package com.itic.intranet.services;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.models.User;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getAllActiveUsers();
    User getUserById(Long id);
    User findByFirstnameOrLastname(String firstname , String lastName);
    User addUser(UserRequestDto userDto);
    User updateUser(Long id, UserRequestDto userDto);
    void deleteUser(Long id);
    void removeUser(Long id);
}

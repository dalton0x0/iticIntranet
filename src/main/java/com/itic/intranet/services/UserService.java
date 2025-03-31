package com.itic.intranet.services;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    List<User> getAllActiveUsers();
    User getUserById(Long id);
    List<User> searchUsers(String keyword);
    User createUser(UserRequestDto userDto);
    User updateUser(Long id, UserRequestDto userDto);
    void deactivateUser(Long id);
    void permanentlyDeleteUser(Long id);
    void assignClassroomToUser(Long userId, Long classroomId);
}
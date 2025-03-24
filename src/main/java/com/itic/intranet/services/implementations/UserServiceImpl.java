package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("No users found", allUsers))
                : ResponseEntity.ok(new ApiResponse("List of all users", allUsers));
    }

    @Override
    public ResponseEntity<ApiResponse> getAllActiveUsers() {
        List<User> activeUsers = userRepository.findByActive(true);
        return activeUsers.isEmpty()
                ? ResponseEntity.ok(new ApiResponse("No active users found", activeUsers))
                : ResponseEntity.ok(new ApiResponse("List of active users", activeUsers));
    }

    @Override
    public ResponseEntity<ApiResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(new ApiResponse("User found", user)))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> findByFirstnameOrLastname(String firstname, String lastName) {
        if ((firstname == null || firstname.trim().isEmpty()) && (lastName == null || lastName.trim().isEmpty())) {
            throw new BadRequestException("Firstname or lastname is required");
        }
        List<User> users = userRepository.findUserByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(firstname, lastName);
        return users.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("No users found", users))
                : ResponseEntity.ok(new ApiResponse("Users found", users));
    }

    @Override
    public ResponseEntity<ApiResponse> addUser(UserRequestDto userDto) {
        validateUserRequest(userDto);
        User newUser = User.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .classrooms(userDto.getClassroom())
                .active(true)
                .build();
        User savedUser = userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User created successfully", savedUser));
    }

    @Override
    public ResponseEntity<ApiResponse> updateUser(Long id, UserRequestDto userDto) {
        validateUserRequest(userDto);
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstname(userDto.getFirstname());
                    existingUser.setLastname(userDto.getLastname());
                    existingUser.setEmail(userDto.getEmail());
                    existingUser.setUsername(userDto.getUsername());
                    existingUser.setPassword(userDto.getPassword());
                    existingUser.setRole(userDto.getRole());
                    existingUser.setClassrooms(userDto.getClassroom());
                    existingUser.setActive(true);
                    User updatedUser = userRepository.save(existingUser);
                    return ResponseEntity.ok(new ApiResponse("User updated successfully", updatedUser));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(false);
                    userRepository.save(user);
                    return ResponseEntity.ok(new ApiResponse("User deactivated successfully", user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> removeUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok(new ApiResponse("User permanently deleted", null));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    private void validateUserRequest(UserRequestDto userDto) {
        if (userDto.getFirstname() == null || userDto.getFirstname().trim().isEmpty()) {
            throw new BadRequestException("Firstname is required");
        }
        if (userDto.getLastname() == null || userDto.getLastname().trim().isEmpty()) {
            throw new BadRequestException("Lastname is required");
        }
        if (userDto.getEmail() == null || !userDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRequestException("Invalid email format");
        }
        if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username is required");
        }
        if (userDto.getPassword() == null || userDto.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }
    }
}

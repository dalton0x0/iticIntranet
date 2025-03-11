package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ApiResponse getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            return new ApiResponse("List of all users is empty", HttpStatus.NO_CONTENT, allUsers);
        }
        return new ApiResponse("List of all users", HttpStatus.OK, allUsers);
    }

    @Override
    public ApiResponse getAllActiveUsers() {
        List<User> activeUsers = userRepository.findByActive(true);
        if (activeUsers.isEmpty()) {
            return new ApiResponse("List of all active users is empty", HttpStatus.OK, activeUsers);
        }
        return new ApiResponse("List of all active users", HttpStatus.OK, activeUsers);
    }

    @Override
    public ApiResponse getUserById(Long id) {
        Optional<User> user = Optional.ofNullable(userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        ));
        return new ApiResponse("User found", HttpStatus.OK, user);
    }


    @Override
    public ApiResponse findByFirstnameOrLastname(String firstname, String lastName) {
        if ( (firstname == null || firstname.isEmpty()) && (lastName == null || lastName.isEmpty()) ) {
            throw new BadRequestException("Firstname or lastname is empty");
        }
        List<User> users = userRepository.findUserByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(firstname, lastName);
        if (users.isEmpty()) {
            return new ApiResponse("Not user found", HttpStatus.NO_CONTENT, users);
        }
        return new ApiResponse("User found", HttpStatus.OK, users);
    }

    @Override
    public ApiResponse addUser(UserRequestDto userDto) {
        var newUser = User.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .active(true)
                .build();
        User savedUser = userRepository.save(newUser);
        return new ApiResponse("User created successfully", HttpStatus.CREATED, savedUser);
    }

    @Override
    public ApiResponse updateUser(Long id, UserRequestDto userDto) {
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        existingUser.setFirstname(userDto.getFirstname());
        existingUser.setLastname(userDto.getLastname());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setUsername(userDto.getUsername());
        existingUser.setPassword(userDto.getPassword());
        existingUser.setActive(true);
        User updatedUser = userRepository.save(existingUser);
        return new ApiResponse("User updated successfully", HttpStatus.OK, updatedUser);
    }

    @Override
    public ApiResponse deleteUser(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        userToDelete.setActive(false);
        userRepository.save(userToDelete);
        return new ApiResponse("User deleted successfully", HttpStatus.OK, null);
    }

    @Override
    public ApiResponse removeUser(Long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        userRepository.delete(userToDelete);
        return new ApiResponse("User removed successfully", HttpStatus.OK, null);
    }
}

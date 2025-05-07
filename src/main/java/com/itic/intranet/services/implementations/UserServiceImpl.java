package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.Role;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAllActiveUsers() {
        return userRepository.findByActive(true)
                .stream()
                .map(userMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.convertEntityToResponseDto(user);
    }

    @Override
    public List<UserResponseDto> searchUser(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(userMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        validateUserRequest(userDto);
        checkUniqueConstraints(userDto);

        Role role = roleRepository.findByRoleType(userDto.getRoleType())
                .orElseThrow(() -> new BadRequestException("Invalid role type"));

        User user = userMapper.convertDtoToEntity(userDto, role);
        User savedUser = userRepository.save(user);
        return userMapper.convertEntityToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateUserRequest(userDto);
        checkUniqueConstraintsForUpdate(id, userDto);

        Role role = roleRepository.findByRoleType(userDto.getRoleType())
                .orElseThrow(() -> new BadRequestException("Invalid role type"));

        userMapper.updateEntityFromDto(userDto, existingUser, role);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.convertEntityToResponseDto(updatedUser);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new BadRequestException("User is already deactivated");
        }

        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void permanentlyDeleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    private void validateUserRequest(UserRequestDto dto) {
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new BadRequestException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new BadRequestException("Last name is required");
        }
        if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
            throw new BadRequestException("Invalid email format");
        }
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username is required");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }
        if (dto.getRoleType() == null) {
            throw new BadRequestException("Role type is required");
        }
    }

    private void checkUniqueConstraints(UserRequestDto dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new BadRequestException("Email already exists");
        });
        userRepository.findByUsername(dto.getUsername()).ifPresent(r -> {
            throw new BadRequestException("Username already exists");
        });
    }

    private void checkUniqueConstraintsForUpdate(Long userId, UserRequestDto dto) {
        Optional<User> existingEmailUser = userRepository.findByEmail(dto.getEmail());
        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(userId)) {
            throw new BadRequestException("This new email is already exists");
        }

        Optional<User> existingUsernameUser = userRepository.findByUsername(dto.getUsername());
        if (existingUsernameUser.isPresent() && !existingUsernameUser.get().getId().equals(userId)) {
            throw new BadRequestException("This new username is already exists");
        }
    }
}

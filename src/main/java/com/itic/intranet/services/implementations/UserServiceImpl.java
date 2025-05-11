package com.itic.intranet.services.implementations;

import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.Role;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EntityHelper entityHelper;

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
        User user = entityHelper.getUser(id);
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
        Role role = entityHelper.getRoleRoleType(userDto.getRoleType());
        User user = userMapper.convertDtoToEntity(userDto, role);
        User savedUser = userRepository.save(user);
        return userMapper.convertEntityToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userDto) {
        User existingUser = entityHelper.getUser(id);
        validateUserRequest(userDto);
        Role role = entityHelper.getRoleRoleType(userDto.getRoleType());
        checkUniqueConstraintsForUpdate(id, userDto);
        userMapper.updateEntityFromDto(userDto, existingUser, role);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.convertEntityToResponseDto(updatedUser);
    }

    @Override
    public void deactivateUser(Long id) {
        User existingUser = entityHelper.getActiveUser(id);
        existingUser.setActive(false);
        userRepository.save(existingUser);
    }

    @Override
    public void permanentlyDeleteUser(Long id) {
        User user = entityHelper.getUser(id);
        userRepository.delete(user);
    }

    private void validateUserRequest(UserRequestDto dto) {
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new BadRequestException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new BadRequestException("Last name is required");
        }
        String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (dto.getEmail() != null && !dto.getEmail().matches(regex)) {
            throw new BadRequestException("Invalid email format");
        }
        if (dto.getEmail() == null) {
            throw new BadRequestException("Email cannot be null");
        }
        if (dto.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email cannot be empty");
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

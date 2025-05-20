package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.mysql.Role;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.security.CustomPasswordEncoder;
import com.itic.intranet.services.LogService;
import com.itic.intranet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EntityHelper entityHelper;
    private final CustomPasswordEncoder passwordEncoder;
    private final LogService logService;

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserResponseDto> allUsers = userRepository.findAll()
                .stream()
                .map(userMapper::convertEntityToResponseDto)
                .toList();
        logService.info("SYSTEM", "GET_ALL_USERS", "Retrieved all users", Map.of("userCount", allUsers.size()));
        return allUsers;
    }

    @Override
    public List<UserResponseDto> getAllActiveUsers() {
        List<UserResponseDto> allActiveUsers = userRepository.findByActive(true)
                .stream()
                .map(userMapper::convertEntityToResponseDto)
                .toList();
        logService.info("SYSTEM", "GET_ALL_ACTIVE_USERS", "Retrieved all active users", Map.of("userCount", allActiveUsers.size()));
        return allActiveUsers;
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = entityHelper.getUser(id);
        logService.info("SYSTEM", "GET_USER", "Retrieved user", Map.of("userId", user.getId()));
        return userMapper.convertEntityToResponseDto(user);
    }

    @Override
    public List<UserResponseDto> searchUser(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        List<UserResponseDto> results = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(userMapper::convertEntityToResponseDto)
                .toList();
        logService.info("SYSTEM", "SEARCH_USER", "Searched users", Map.of("keyword", keyword, "resultCount", results.size()));
        return results;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        validateUserRequest(userDto);
        checkUniqueConstraints(userDto);
        Role role = entityHelper.getRoleRoleType(userDto.getRoleType());
        User user = userMapper.convertDtoToEntity(userDto, role);
        User savedUser = userRepository.save(user);
        logService.info("SYSTEM", "CREATE_USER", "Created new user", Map.of("userId", savedUser.getId(), "role", role.getRoleType()));
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
        logService.info("SYSTEM", "UPDATE_USER", "Updated user", Map.of("userId", updatedUser.getId(), "role", role.getRoleType()));
        return userMapper.convertEntityToResponseDto(updatedUser);
    }

    @Override
    public void deactivateUser(Long id) {
        User existingUser = entityHelper.getActiveUser(id);
        existingUser.setActive(false);
        userRepository.save(existingUser);
        logService.info("SYSTEM", "DEACTIVATE_USER", "Deactivated user", Map.of("userId", id));
    }

    @Override
    public void permanentlyDeleteUser(Long id) {
        User user = entityHelper.getUser(id);
        userRepository.delete(user);
        logService.info("SYSTEM", "DELETE_USER", "Deleted user", Map.of("userId", id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User account does not exists !")
        );
    }

    private void validateUserRequest(UserRequestDto dto) {
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            throw new BadRequestException("First name is required");
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new BadRequestException("Last name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email is required");
        }
        String regexEmail = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (!dto.getEmail().matches(regexEmail)) {
            throw new BadRequestException("Invalid email format");
        }
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username is required");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password is required");
        }
        if (dto.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }

        String regexPasswordUppercaseLetter = ".*[A-Z].*";
        String regexPasswordLowercaseLetter = ".*[a-z].*";
        String regexPasswordDigit = ".*[0-9].*";
        String regexPasswordSpecialCharacter = ".*[#?!@$%^&*-].*";

        if (!dto.getPassword().matches(regexPasswordUppercaseLetter)) {
            throw new BadRequestException("Password must contain at least one uppercase letter");
        }
        if (!dto.getPassword().matches(regexPasswordLowercaseLetter)) {
            throw new BadRequestException("Password must contain at least one lowercase letter");
        }
        if (!dto.getPassword().matches(regexPasswordDigit)) {
            throw new BadRequestException("Password must contain at least one digit");
        }
        if (!dto.getPassword().matches(regexPasswordSpecialCharacter)) {
            throw new BadRequestException("Password must contain at least one special character");
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
        User user = entityHelper.getUser(userId);
        if (user.getPassword() != null && !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }
}

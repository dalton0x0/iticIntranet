package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.Role;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ClassroomRepository classroomRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllActiveUsers() {
        return userRepository.findByActive(true);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Incorrect search");
        }
        return userRepository.findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public User createUser(UserRequestDto userDto) {
        validateUserRequest(userDto);
        checkUniqueConstraints(userDto);
        User user = new User();
        mapDtoToEntity(userDto, user);
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserRequestDto userDto) {
        User user = getUserById(id);
        validateUserRequest(userDto);
        checkUniqueConstraints(userDto);
        mapDtoToEntity(userDto, user);
        return userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        if (!user.isActive()) {
            throw new BadRequestException("The user is already deactivated");
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void permanentlyDeleteUser(Long id) {
        User user = getUserById(id);
        userRepository.deleteById(user.getId());
    }

    @Override
    public void addRoleToUser(Long userId, Long roleId) {
        User user = getUserById(userId);
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role not found")
        );
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        User user = getUserById(userId);
        if (user.getRole() == null) {
            throw new ResourceNotFoundException("This user does not have a role");
        }
        user.setRole(null);
        userRepository.save(user);
    }

    @Override
    public void assignClassroomToUser(Long userId, Long classroomId) {
        User user = getUserById(userId);
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (user.isTeacher()) {
            if (user.getTaughtClassrooms().contains(classroom)) {
                throw new BadRequestException("This teacher is already assigned to this classroom");
            }
            user.getTaughtClassrooms().add(classroom);
        } else if (user.isStudent()) {
            if (user.getClassroom() != null) {
                throw new BadRequestException("A student can only have one class");
            }
            user.setClassroom(classroom);
        }
        userRepository.save(user);
    }

    @Override
    public void removeClassroomFromUser(Long userId, Long classroomId) {
        User user = getUserById(userId);
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (user.isTeacher()) {
            if (!user.getTaughtClassrooms().contains(classroom)) {
                throw new BadRequestException("This teacher is already removed from this classroom");
            }
            user.getTaughtClassrooms().remove(classroom);
        } else
        if (user.isStudent()) {
            if (user.getClassroom() == null) {
                throw new BadRequestException("This student is already removed from classroom");
            }
            user.setClassroom(null);
        }
        userRepository.save(user);
    }

    private void validateUserRequest(UserRequestDto dto) {
        if (dto.getFirstname() == null || dto.getFirstname().trim().isEmpty()) {
            throw new BadRequestException("Firstname is required");
        }
        if (dto.getLastname() == null || dto.getLastname().trim().isEmpty()) {
            throw new BadRequestException("Lastname is required");
        }
        if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
            throw new BadRequestException("Invalid email");
        }
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Username is required");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }
    }

    private void checkUniqueConstraints(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("This email address already exists");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
    }

    private void mapDtoToEntity(UserRequestDto dto, User user) {
        user.setFirstname(dto.getFirstname().trim());
        user.setLastname(dto.getLastname().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setUsername(dto.getUsername().trim());
        user.setPassword(dto.getPassword());

        Role role = roleRepository.findByRoleType(dto.getRoleType())
                .orElseThrow(() -> new BadRequestException("Invalid RoleType"));
        user.setRole(role);
    }
}
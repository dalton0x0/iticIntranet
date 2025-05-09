package com.itic.intranet.services.implementations;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.Role;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPropertyServiceImpl implements UserPropertyService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClassroomRepository classroomRepository;

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (user.getRole() == null) {
            throw new BadRequestException("User has no role assigned");
        }
        if (!user.getRole().equals(role)) {
            throw new BadRequestException("User has not this role assigned");
        }
        user.setRole(null);
        userRepository.save(user);
    }

    @Override
    public RoleType getRoleOfUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == null) {
            throw new BadRequestException("User has no role assigned");
        }
        return user.getRole().getRoleType();
    }

    @Override
    public void assignClassroomToUser(Long userId, Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (user.isTeacher()) {
            if (user.getTaughtClassrooms().contains(classroom)) {
                throw new BadRequestException("This teacher is already added in this classroom");
            }
            user.getTaughtClassrooms().add(classroom);
        } else if (user.isStudent()) {
            if (user.getClassroom() != null) {
                throw new BadRequestException("This student is already assigned to a class");
            }
            user.setClassroom(classroom);
        } else if (user.getRole().getRoleType() == RoleType.ADMIN) {
            throw new BadRequestException("Admin cannot be assigned to a classroom");
        }
        userRepository.save(user);
    }

    @Override
    public void removeClassroomFromUser(Long userId, Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (user.isTeacher()) {
            if (!user.getTaughtClassrooms().contains(classroom)) {
                throw new BadRequestException("This teacher is already removed in this classroom");
            }
            user.getTaughtClassrooms().remove(classroom);
        }
        if (user.isStudent()) {
            if (user.getClassroom() == null) {
                throw new BadRequestException("This student is not assigned to a class");
            }
            if (!user.getClassroom().getId().equals(classroomId)) {
                throw new BadRequestException("This student is not assigned to this classroom");
            }
            user.setClassroom(null);
        }
        userRepository.save(user);
    }
}

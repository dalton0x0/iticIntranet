package com.itic.intranet.services.implementations;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.models.mysql.Classroom;
import com.itic.intranet.models.mysql.Role;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.UserPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPropertyServiceImpl implements UserPropertyService {

    private final UserRepository userRepository;
    private final EntityHelper entityHelper;

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = entityHelper.getUser(userId);
        Role role = entityHelper.getRole(roleId);
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        User user = entityHelper.getUser(userId);
        Role role = entityHelper.getRole(roleId);
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
        User user = entityHelper.getUser(userId);
        if (user.getRole() == null) {
            throw new BadRequestException("User has no role assigned");
        }
        return user.getRole().getRoleType();
    }

    @Override
    public void assignClassroomToUser(Long userId, Long classroomId) {
        Classroom classroom = entityHelper.getClassroom(classroomId);
        User user = entityHelper.getUser(userId);
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
        Classroom classroom = entityHelper.getClassroom(classroomId);
        User user = entityHelper.getUser(userId);
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

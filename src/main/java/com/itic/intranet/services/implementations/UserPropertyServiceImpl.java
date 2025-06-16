package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteMinimalDto;
import com.itic.intranet.enums.LogActor;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.NoteMapper;
import com.itic.intranet.models.mysql.Classroom;
import com.itic.intranet.models.mysql.Role;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.LogService;
import com.itic.intranet.services.UserPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserPropertyServiceImpl implements UserPropertyService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final EntityHelper entityHelper;
    private final LogService logService;

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = entityHelper.getUser(userId);
        Role role = entityHelper.getRole(roleId);
        user.setRole(role);
        userRepository.save(user);
        logService.info(
                LogActor.SYSTEM.name(),
                "ASSIGN_ROLE",
                "Assigning role",
                Map.of(
                        "userName", user.getFullName(),
                        "roleAssigned", user.getRole().getRoleType()
                )
        );
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
        logService.info(
                LogActor.SYSTEM.name(),
                "REMOVE_ROLE",
                "Removing role",
                Map.of(
                        "userName", user.getFullName(),
                        "roleRemoved", role.getRoleType()
                )
        );
    }

    @Override
    public RoleType getRoleOfUser(Long userId) {
        User user = entityHelper.getUser(userId);
        if (user.getRole() == null) {
            throw new BadRequestException("User has no role assigned");
        }
        logService.info(
                LogActor.SYSTEM.name(),
                "GET_ROLE_OF_USER",
                "Getting role of user",
                Map.of(
                        "userId", userId,
                        "userName", user.getFullName(),
                        "roleFound", user.getRole().getRoleType()
                )
        );
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
        logService.info(
                LogActor.SYSTEM.name(),
                "ASSIGN_CLASSROOM_TO_USER",
                "Assigning classroom",
                Map.of(
                        "userName", user.getFullName(),
                        "classAssigned", classroom.getName()
                )
        );
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
        logService.info(
                LogActor.SYSTEM.name(),
                "REMOVE_CLASSROOM_TO_USER",
                "Removing classroom",
                Map.of(
                        "userName", user.getFullName(),
                        "classRemoved", classroom.getName()
                )
        );
    }

    @Override
    public List<NoteMinimalDto> getStudentNotes(Long studentId) {
        List<NoteMinimalDto> userNotes = noteRepository.findByStudentId(studentId)
                .stream()
                .map(noteMapper::convertEntityToUserMinimalDto)
                .toList();
        logService.info(
                LogActor.SYSTEM.name(),
                "GET_STUDENT_NOTES",
                "Getting student notes",
                Map.of(
                        "allNotesCounted", userNotes.size()
                )
        );
        return userNotes;
    }
}

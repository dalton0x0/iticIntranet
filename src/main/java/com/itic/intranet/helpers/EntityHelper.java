package com.itic.intranet.helpers;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.*;
import com.itic.intranet.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityHelper {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClassroomRepository classroomRepository;
    private final EvaluationRepository evaluationRepository;
    private final NoteRepository noteRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
    }

    public User getActiveUser(Long id) {
        User user = getUser(id);
        if (!user.isActive()) {
            throw new ResourceNotFoundException("User is not active");
        }
        return user;
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found")
        );
    }

    public Role getRoleRoleType(RoleType roleType) {
        return roleRepository.findByRoleType(roleType).orElseThrow(
                () -> new ResourceNotFoundException("Role type not found")
        );
    }

    public Classroom getClassroom(Long id) {
        return classroomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Classroom not found")
        );
    }

    public Evaluation getEvaluation(Long id) {
        return evaluationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        );
    }

    public Note getNote(Long id) {
        return noteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Note not found")
        );
    }
}

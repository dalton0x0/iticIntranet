package com.itic.intranet.services;

import com.itic.intranet.dtos.NoteMinimalDto;
import com.itic.intranet.enums.RoleType;

import java.util.List;

public interface UserPropertyService {

    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId, Long roleId);
    RoleType getRoleOfUser(Long userId);

    void assignClassroomToUser(Long userId, Long classroomId);
    void removeClassroomFromUser(Long userId, Long classroomId);

    List<NoteMinimalDto> getStudentNotes(Long studentId);
}

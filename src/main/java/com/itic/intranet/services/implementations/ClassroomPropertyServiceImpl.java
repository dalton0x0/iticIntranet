package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.ClassroomPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomPropertyServiceImpl implements ClassroomPropertyService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserMinimalDto> getTeachersOfClassroom(Long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResourceNotFoundException("Classroom not found");
        }
        List<User> teachers = userRepository.findTeachersByClassroomId(classroomId);
        return teachers.stream()
                .map(userMapper::convertEntityToUserMinimalDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMinimalDto> getStudentsOfClassroom(Long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResourceNotFoundException("Classroom not found");
        }
        List<User> students = userRepository.findByClassroomIdAndRoleType(classroomId, RoleType.STUDENT);
        return students.stream()
                .map(userMapper::convertEntityToUserMinimalDto)
                .collect(Collectors.toList());
    }
}

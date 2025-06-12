package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.EvaluationMapper;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.ClassroomPropertyService;
import com.itic.intranet.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClassroomPropertyServiceImpl implements ClassroomPropertyService {

    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserMapper userMapper;
    private final EvaluationMapper evaluationMapper;
    private final EntityHelper entityHelper;
    private final LogService logService;

    @Override
    public List<UserMinimalDto> getClassroomTeachers(Long classroomId) {
        entityHelper.getClassroom(classroomId);
        List<UserMinimalDto> allTeachers = userRepository.findTeachersByClassroomId(classroomId)
                .stream()
                .map(userMapper::convertEntityToUserMinimalDto)
                .toList();
        logService.info(
                "SYSTEM",
                "GET_CLASSROOM_TEACHERS",
                "Getting all teachers of classroom",
                Map.of(
                        "allTeachersClassroomCounted", allTeachers.size()
                )
        );
        return allTeachers;
    }

    @Override
    public List<UserMinimalDto> getClassroomStudents(Long classroomId) {
        entityHelper.getClassroom(classroomId);
        List<UserMinimalDto> allStudents = userRepository.findByClassroomIdAndRoleType(classroomId, RoleType.STUDENT)
                .stream()
                .map(userMapper::convertEntityToUserMinimalDto)
                .toList();
        logService.info(
                "SYSTEM",
                "GET_CLASSROOM_STUDENTS",
                "Getting all students of classroom",
                Map.of(
                        "allStudentClassroomCounted", allStudents.size()
                )
        );
        return allStudents;
    }

    @Override
    public List<EvaluationResponseDto> getClassroomEvaluations(Long classroomId) {
        entityHelper.getClassroom(classroomId);
        List<EvaluationResponseDto> allEvaluations = evaluationRepository.findEvaluationByClassroomsId(classroomId)
                .stream()
                .map(evaluationMapper::convertEntityToResponseDto)
                .toList();
        logService.info(
                "SYSTEM",
                "GET_CLASSROOM_EVALUATIONS",
                "Getting all evaluations of classroom",
                Map.of(
                        "allEvaluationsClassroomCounted", allEvaluations.size()
                )
        );
        return allEvaluations;
    }
}

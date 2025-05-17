package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.EvaluationMapper;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.ClassroomPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomPropertyServiceImpl implements ClassroomPropertyService {

    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserMapper userMapper;
    private final EvaluationMapper evaluationMapper;
    private final EntityHelper entityHelper;

    @Override
    public List<UserMinimalDto> getTeachersOfClassroom(Long classroomId) {
        entityHelper.getClassroom(classroomId);
        List<User> teachers = userRepository.findTeachersByClassroomId(classroomId);
        return teachers.stream()
                .map(userMapper::convertEntityToUserMinimalDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMinimalDto> getStudentsOfClassroom(Long classroomId) {
        entityHelper.getClassroom(classroomId);
        List<User> students = userRepository.findByClassroomIdAndRoleType(classroomId, RoleType.STUDENT);
        return students.stream()
                .map(userMapper::convertEntityToUserMinimalDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationResponseDto> getEvaluationsOfClassroom(Long classroomId) {
        entityHelper.getClassroom(classroomId);
        List<Evaluation> evaluations = evaluationRepository.findEvaluationByClassroomsId(classroomId);
        return evaluations.stream()
                .map(evaluationMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }
}

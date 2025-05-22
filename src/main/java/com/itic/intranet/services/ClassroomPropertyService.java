package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;

import java.util.List;

public interface ClassroomPropertyService {
    List<UserMinimalDto> getClassroomTeachers(Long classroomId);
    List<UserMinimalDto> getClassroomStudents(Long classroomId);
    List<EvaluationResponseDto> getClassroomEvaluations(Long classroomId);
}

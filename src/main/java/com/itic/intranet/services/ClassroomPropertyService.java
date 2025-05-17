package com.itic.intranet.services;

import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;

import java.util.List;

public interface ClassroomPropertyService {
    List<UserMinimalDto> getTeachersOfClassroom(Long classroomId);
    List<UserMinimalDto> getStudentsOfClassroom(Long classroomId);
    List<EvaluationResponseDto> getEvaluationsOfClassroom(Long classroomId);
}

package com.itic.intranet.services;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface ClassroomService {
    ApiResponse getAllClassrooms();
    ApiResponse getClassroomById(Long id);
    ApiResponse createClassroom(ClassroomRequestDto classroomDto);
    ApiResponse updateClassroom(Long id, ClassroomRequestDto classroomDto);
    ApiResponse deleteClassroom(Long id);
    ApiResponse addStudentToClassroom(Long classroomId, Long studentId);
    ApiResponse removeStudentFromClassroom(Long classroomId, Long studentId);
    ApiResponse getClassroomStudents(Long classroomId);
    ApiResponse getClassroomTeachers(Long classroomId);
}
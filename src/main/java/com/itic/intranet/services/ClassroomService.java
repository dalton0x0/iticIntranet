package com.itic.intranet.services;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.utils.ApiResponse;

public interface ClassroomService {
    ApiResponse getAllClassrooms();
    ApiResponse getClassroomById(Long id);
    ApiResponse getClassroomByName(String classroomName);
    ApiResponse addClassroom(ClassroomRequestDto classroomDto);
    ApiResponse updateClassroom(Long id, ClassroomRequestDto classroomDto);
    ApiResponse deleteClassroom(Long id);
}

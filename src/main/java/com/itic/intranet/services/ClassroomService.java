package com.itic.intranet.services;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.ClassroomResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.models.Classroom;

import java.util.List;

public interface ClassroomService {
    List<ClassroomResponseDto> getAllClassrooms();
    ClassroomResponseDto getClassroomById(Long id);
    List<ClassroomResponseDto> searchClassroom(String keyword);
    ClassroomResponseDto createClassroom(ClassroomRequestDto classroomDto);
    ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto classroomDto);
    void deleteClassroom(Long id);
}

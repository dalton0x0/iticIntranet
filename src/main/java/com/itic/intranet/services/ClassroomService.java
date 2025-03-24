package com.itic.intranet.services;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ClassroomService {
    ResponseEntity<ApiResponse> getAllClassrooms();
    ResponseEntity<ApiResponse> getClassroomById(Long id);
    ResponseEntity<ApiResponse> getClassroomByName(String classroomName);
    ResponseEntity<ApiResponse> addClassroom(ClassroomRequestDto classroomDto);
    ResponseEntity<ApiResponse> updateClassroom(Long id, ClassroomRequestDto classroomDto);
    ResponseEntity<ApiResponse> deleteClassroom(Long id);
}

package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.services.ClassroomService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllClassrooms() {
        List<Classroom> allClassrooms = classroomRepository.findAll();
        return allClassrooms.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("No classrooms found", allClassrooms))
                : ResponseEntity.ok(new ApiResponse("List of classrooms", allClassrooms));
    }

    @Override
    public ResponseEntity<ApiResponse> getClassroomById(Long id) {
        return classroomRepository.findById(id)
                .map(classroom -> ResponseEntity.ok(new ApiResponse("Classroom found", classroom)))
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> getClassroomByName(String classroomName) {
        List<Classroom> classrooms = classroomRepository.findByNameContaining(classroomName);
        if (classrooms.isEmpty()) {
            throw new ResourceNotFoundException("Classroom not found with name: " + classroomName);
        }
        return ResponseEntity.ok(new ApiResponse("Classroom(s) found", classrooms));
    }

    @Override
    public ResponseEntity<ApiResponse> addClassroom(ClassroomRequestDto classroomDto) {
        if (classroomDto.getName() == null || classroomDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Classroom name is required");
        }
        Classroom newClassroom = Classroom.builder()
                .name(classroomDto.getName())
                .build();
        Classroom savedClassroom = classroomRepository.save(newClassroom);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Classroom created successfully", savedClassroom));
    }

    @Override
    public ResponseEntity<ApiResponse> updateClassroom(Long id, ClassroomRequestDto classroomDto) {
        if (classroomDto.getName() == null || classroomDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Classroom name is required");
        }
        return classroomRepository.findById(id)
                .map(existingClassroom -> {
                    existingClassroom.setName(classroomDto.getName());
                    Classroom updatedClassroom = classroomRepository.save(existingClassroom);
                    return ResponseEntity.ok(new ApiResponse("Classroom updated successfully", updatedClassroom));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with ID: " + id));
    }

    @Override
    public ResponseEntity<ApiResponse> deleteClassroom(Long id) {
        return classroomRepository.findById(id)
                .map(classroom -> {
                    classroomRepository.delete(classroom);
                    return ResponseEntity.ok(new ApiResponse("Classroom deleted successfully", null));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with ID: " + id));
    }
}

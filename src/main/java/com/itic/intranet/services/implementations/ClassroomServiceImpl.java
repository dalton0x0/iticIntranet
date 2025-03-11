package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.services.ClassroomService;
import com.itic.intranet.utils.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    ClassroomRepository classroomRepository;

    @Override
    public ApiResponse getAllClassrooms() {
        List<Classroom> allClassrooms = classroomRepository.findAll();
        if (allClassrooms.isEmpty()) {
            return new ApiResponse("List of classrooms is empty", HttpStatus.NO_CONTENT, allClassrooms);
        }
        return new ApiResponse("List of classrooms", HttpStatus.OK, allClassrooms);
    }

    @Override
    public ApiResponse getClassroomById(Long id) {
        Optional<Classroom> classroom = Optional.ofNullable(classroomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Classroom not found")
        ));
        return new ApiResponse("Classroom found", HttpStatus.OK, classroom);
    }

    @Override
    public ApiResponse getClassroomByName(String name) {
        Optional<Classroom> classroom = Optional.ofNullable(classroomRepository.findByNameContaining(name).orElseThrow(
                () -> new ResourceNotFoundException("Classroom not found")
        ));
        return new ApiResponse("Classroom found", HttpStatus.OK, classroom);
    }

    @Override
    public ApiResponse addClassroom(ClassroomRequestDto classroomDto) {
        var newClassroom = Classroom.builder()
                .name(classroomDto.getName())
                .build();
        Classroom savedClassroom = classroomRepository.save(newClassroom);
        return new ApiResponse("Classroom created successfully", HttpStatus.CREATED, savedClassroom);
    }

    @Override
    public ApiResponse updateClassroom(Long id, ClassroomRequestDto classroomDto) {
        Classroom existingClassroom = classroomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Classroom not found")
        );
        existingClassroom.setName(classroomDto.getName());
        Classroom updatedClassroom = classroomRepository.save(existingClassroom);
        return new ApiResponse("Classroom updated successfully", HttpStatus.OK, updatedClassroom);
    }

    @Override
    public ApiResponse deleteClassroom(Long id) {
        Classroom classroomToDelete = classroomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Classroom not found")
        );
        classroomRepository.delete(classroomToDelete);
        return new ApiResponse("Classroom deleted successfully", HttpStatus.OK, null);
    }
}

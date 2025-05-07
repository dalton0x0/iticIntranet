package com.itic.intranet.mappers;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.ClassroomResponseDto;
import com.itic.intranet.models.Classroom;
import org.springframework.stereotype.Component;

@Component
public class ClassroomMapper {

    public Classroom convertDtoToEntity(ClassroomRequestDto classroomDto) {
        return Classroom.builder()
                .id(classroomDto.getId())
                .name(classroomDto.getName().trim())
                .build();
    }

    public ClassroomResponseDto convertEntityToResponseDto(Classroom classroom) {
        return ClassroomResponseDto.builder()
                .id(classroom.getId())
                .name(classroom.getName())
                .build();
    }

    public void updateEntityFromDto(ClassroomRequestDto classroomDto, Classroom classroom) {
        classroom.setName(classroomDto.getName().trim());
    }
}

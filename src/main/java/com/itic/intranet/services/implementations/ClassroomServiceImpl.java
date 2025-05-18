package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.ClassroomResponseDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.ClassroomMapper;
import com.itic.intranet.models.mysql.Classroom;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final ClassroomMapper classroomMapper;
    private final EntityHelper entityHelper;

    @Override
    public List<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .stream()
                .map(classroomMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClassroomResponseDto getClassroomById(Long id) {
        Classroom classroom = entityHelper.getClassroom(id);
        return classroomMapper.convertEntityToResponseDto(classroom);
    }

    @Override
    public List<ClassroomResponseDto> searchClassroom(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        return classroomRepository.findByNameContaining(keyword)
                .stream()
                .map(classroomMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClassroomResponseDto createClassroom(ClassroomRequestDto classroomDto) {
        validateClassroomRequest(classroomDto);
        checkUniqueConstraints(classroomDto);
        Classroom classroom = classroomMapper.convertDtoToEntity(classroomDto);
        Classroom savedClassroom = classroomRepository.save(classroom);
        return classroomMapper.convertEntityToResponseDto(savedClassroom);
    }

    @Override
    public ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto classroomDto) {
        Classroom existingClassroom = entityHelper.getClassroom(id);
        validateClassroomRequest(classroomDto);
        checkUniqueConstraintsForUpdate(id, classroomDto);
        classroomMapper.updateEntityFromDto(classroomDto, existingClassroom);
        Classroom updatedClassroom = classroomRepository.save(existingClassroom);
        return classroomMapper.convertEntityToResponseDto(updatedClassroom);
    }

    @Override
    public void deleteClassroom(Long id) {
        Classroom classroom = entityHelper.getClassroom(id);
        long studentCount = userRepository.countByClassroomIdAndRoleType(id, RoleType.STUDENT);
        if (studentCount > 0) {
            throw new BadRequestException("Unable to delete this classroom, this content " + studentCount + " student");
        }
        classroomRepository.delete(classroom);
    }

    private void validateClassroomRequest(ClassroomRequestDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new BadRequestException("Name is required");
        }
    }

    private void checkUniqueConstraints(ClassroomRequestDto dto) {
        if (classroomRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Classroom already exists");
        }
    }

    private void checkUniqueConstraintsForUpdate(Long classroomId, ClassroomRequestDto dto) {
        Optional<Classroom> existingNameClassroom = classroomRepository.findByName(dto.getName());
        if (existingNameClassroom.isPresent() && !existingNameClassroom.get().getId().equals(classroomId)) {
            throw new BadRequestException("This new name is already exists");
        }
    }
}

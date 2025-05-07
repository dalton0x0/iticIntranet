package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.ClassroomResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.mappers.ClassroomMapper;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassroomMapper classroomMapper;

    @Override
    public List<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .stream()
                .map(classroomMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClassroomResponseDto getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
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
        Classroom existingClassroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        validateClassroomRequest(classroomDto);
        checkUniqueConstraintsForUpdate(id, classroomDto);
        classroomMapper.updateEntityFromDto(classroomDto, existingClassroom);
        Classroom updatedClassroom = classroomRepository.save(existingClassroom);
        return classroomMapper.convertEntityToResponseDto(updatedClassroom);
    }

    @Override
    public void deleteClassroom(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Classroom not found");
        }

        long studentCount = userRepository.countByClassroomIdAndRoleType(id, RoleType.STUDENT);
        if (studentCount > 0) {
            throw new BadRequestException("Unable to delete this classroom, this content " + studentCount + " student");
        }
        classroomRepository.deleteById(id);
    }

    @Override
    public void addUserToClassroom(Long classroomId, Long studentId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (user.isTeacher()) {
            if (user.getTaughtClassrooms().contains(classroom)) {
                throw new BadRequestException("This teacher is already added in this classroom");
            }
            user.getTaughtClassrooms().add(classroom);
        } else if (user.isStudent()) {
            if (user.getClassroom() != null) {
                throw new BadRequestException("This student is already assigned to a class");
            }
            user.setClassroom(classroom);
        }

        userRepository.save(user);
    }

    @Override
    public void removeUserFromClassroom(Long classroomId, Long studentId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (user.isTeacher()) {
            if (!user.getTaughtClassrooms().contains(classroom)) {
                throw new BadRequestException("This teacher is already removed in this classroom");
            }
            user.getTaughtClassrooms().remove(classroom);
        } else if (user.isStudent()) {
            if (!user.getClassroom().equals(classroom)) {
                throw new BadRequestException("This student is already removed in this classroom");
            }
            user.setClassroom(null);
        }

        userRepository.save(user);
    }

    @Override
    public List<UserMinimalDto> getClassroomStudents(Long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResourceNotFoundException("Classroom not found");
        }

        List<User> students = userRepository.findByClassroomIdAndRoleType(classroomId, RoleType.STUDENT);
        return students.stream().map(this::convertToUserMinimalDto).toList();
    }

    @Override
    public List<UserMinimalDto> getClassroomTeachers(Long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResourceNotFoundException("Classroom not found");
        }

        List<User> teachers = userRepository.findTeachersByClassroomId(classroomId);
        return teachers.stream().map(this::convertToUserMinimalDto).toList();
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

    private UserMinimalDto convertToUserMinimalDto(User user) {
        return new UserMinimalDto(user.getId(), user.getFirstName(), user.getLastName());
    }
}

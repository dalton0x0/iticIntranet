package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.ClassroomDetailedResponseDto;
import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.ClassroomResponseDto;
import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
    }

    @Override
    public Classroom createClassroom(ClassroomRequestDto classroomDto) {
        validateClassroomRequest(classroomDto);
        checkIfClassroomExistsByName(classroomDto);
        Classroom classroom = new Classroom();
        classroom.setName(classroomDto.getName());
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom updateClassroom(Long id, ClassroomRequestDto classroomDto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        validateClassroomRequest(classroomDto);
        checkIfClassroomExistsByName(classroomDto);
        classroom.setName(classroomDto.getName());

        return classroomRepository.save(classroom);
    }

    @Override
    public void deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        long studentCount = userRepository.countByClassroomIdAndRoleType(id, RoleType.STUDENT);
        if (studentCount > 0) {
            throw new BadRequestException("Impossible de supprimer, la classe contient " + studentCount + " étudiant");
        }

        classroomRepository.delete(classroom);
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

    private void checkIfClassroomExistsByName(ClassroomRequestDto dto) {
        if (classroomRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Classroom already exists");
        }
    }

    private ClassroomResponseDto convertToDto(Classroom classroom) {
        return ClassroomResponseDto.builder()
                .id(classroom.getId())
                .name(classroom.getName())
                .build();
    }

    private ClassroomDetailedResponseDto convertToDetailedDto(Classroom classroom) {
        ClassroomDetailedResponseDto dto = new ClassroomDetailedResponseDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());

        dto.setStudents(userRepository.findByClassroomIdAndRoleType(
                classroom.getId(),
                RoleType.STUDENT
        ).stream().map(this::convertToUserMinimalDto).toList());

        return dto;
    }

    private UserMinimalDto convertToUserMinimalDto(User user) {
        return new UserMinimalDto(user.getId(), user.getFirstName(), user.getLastName());
    }
}
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
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse getAllClassrooms() {
        List<Classroom> classrooms = classroomRepository.findAll();

        return ApiResponse.builder()
                .message("Liste des classes")
                .response(classrooms.stream().map(this::convertToDto).toList())
                .build();
    }

    @Override
    public ApiResponse getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée"));

        return ApiResponse.builder()
                .message("Classe trouvée")
                .response(convertToDetailedDto(classroom))
                .build();
    }

    @Override
    public ApiResponse createClassroom(ClassroomRequestDto classroomDto) {
        validateClassroomRequest(classroomDto);

        Classroom classroom = new Classroom();
        classroom.setName(classroomDto.getName());

        Classroom savedClassroom = classroomRepository.save(classroom);
        return ApiResponse.builder()
                .message("Classe créée avec succès")
                .response(convertToDto(savedClassroom))
                .build();
    }

    @Override
    public ApiResponse updateClassroom(Long id, ClassroomRequestDto classroomDto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée"));

        validateClassroomRequest(classroomDto);
        classroom.setName(classroomDto.getName());

        Classroom updatedClassroom = classroomRepository.save(classroom);
        return ApiResponse.builder()
                .message("Classe mise à jour")
                .response(convertToDto(updatedClassroom))
                .build();
    }

    @Override
    public ApiResponse deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée"));

        long studentCount = userRepository.countByClassroomIdAndRoleType(id, RoleType.STUDENT);
        if (studentCount > 0) {
            throw new BadRequestException("Impossible de supprimer : la classe contient " + studentCount + " étudiant(s)");
        }

        classroomRepository.delete(classroom);
        return ApiResponse.builder()
                .message("Classe supprimée")
                .build();
    }

    @Override
    public ApiResponse addStudentToClassroom(Long classroomId, Long studentId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        if (!student.getRole().getRoleType().equals(RoleType.STUDENT)) {
            throw new BadRequestException("Seuls les étudiants peuvent être ajoutés à une classe");
        }

        if (student.getClassroom() != null) {
            throw new BadRequestException("L'étudiant est déjà dans une classe");
        }

        student.setClassroom(classroom);
        userRepository.save(student);

        return ApiResponse.builder()
                .message("Étudiant ajouté à la classe")
                .response(convertToDetailedDto(classroom))
                .build();
    }

    @Override
    public ApiResponse removeStudentFromClassroom(Long classroomId, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        if (student.getClassroom() == null || !student.getClassroom().getId().equals(classroomId)) {
            throw new BadRequestException("L'étudiant n'est pas dans cette classe");
        }

        student.setClassroom(null);
        userRepository.save(student);

        return ApiResponse.builder()
                .message("Étudiant retiré de la classe")
                .build();
    }

    @Override
    public ApiResponse getClassroomStudents(Long classroomId) {

        if (!classroomRepository.existsById(classroomId)) {
            throw new ResourceNotFoundException("Classe non trouvée");
        }

        List<User> students = userRepository.findByClassroomIdAndRoleType(
                classroomId,
                RoleType.STUDENT
        );

        return ApiResponse.builder()
                .message(students.size() + " étudiant(s) trouvé(s)")
                .response(students.stream()
                        .map(this::convertToUserMinimalDto)
                        .toList())
                .build();
    }

    @Override
    public ApiResponse getClassroomTeachers(Long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResourceNotFoundException("Classe non trouvée");
        }
        List<User> teachers = userRepository.findTeachersByClassroomId(classroomId);

        return ApiResponse.builder()
                .message(teachers.size() + " enseignant(s) trouvé(s)")
                .response(teachers.stream()
                        .map(this::convertToUserMinimalDto)
                        .toList())
                .build();
    }

    private void validateClassroomRequest(ClassroomRequestDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new BadRequestException("Le nom de la classe est obligatoire");
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
        return UserMinimalDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }
}

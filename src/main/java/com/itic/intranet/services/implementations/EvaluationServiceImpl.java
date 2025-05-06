package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.Note;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public List<EvaluationDetailedResponseDto> getAllEvaluations() {
        return evaluationRepository.findAll().stream().map(this::convertToDetailedDto).toList();
    }

    @Override
    public Evaluation getEvaluationById(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found"));
    }

    @Override
    public List<Evaluation> searchEvaluations(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestException("Incorrect search");
        }
        return evaluationRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Evaluation createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        if (!user.isTeacher()) {
            throw new BadRequestException("Only teacher can create evaluations");
        }

        validateEvaluationRequest(evaluationDto);
        Evaluation evaluation = new Evaluation();
        mapToDtoEntity(evaluationDto, evaluation);
        evaluation.setCreatedBy(user);

        return evaluationRepository.save(evaluation);
    }

    @Override
    public Evaluation updateEvaluation(Long id, EvaluationRequestDto evaluationDto) {
        Evaluation evaluation = getEvaluationById(id);
        validateEvaluationRequest(evaluationDto);
        mapToDtoEntity(evaluationDto, evaluation);

        return evaluationRepository.save(evaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        Evaluation evaluation = getEvaluationById(id);

        if (!evaluation.getNotes().isEmpty()) {
            throw new BadRequestException("Cannot delete: Evaluation has associated notes");
        }

        evaluationRepository.delete(evaluation);
    }

    @Override
    public void addClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation = getEvaluationById(evaluationId);

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("This evaluation has already associated to this classrooms");
        }

        evaluation.getClassrooms().add(classroom);
        evaluationRepository.save(evaluation);
    }

    @Override
    public void removeClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation = getEvaluationById(evaluationId);

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        if (!evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("This evaluation has been already removed to this classroom");
        }

        evaluation.getClassrooms().remove(classroom);
        evaluationRepository.save(evaluation);
    }

    private void validateEvaluationRequest(EvaluationRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Title is required");
        }
        if (dto.getMinValue() < 0) {
            throw new BadRequestException("The minimum note cannot be negative");
        }
        if (dto.getMaxValue() > 100) {
            throw new BadRequestException("The maximum note cannot exceed 100");
        }
        if (dto.getMinValue() >= dto.getMaxValue()) {
            throw new BadRequestException("The minimum note must be lower than the maximum score");
        }
        if (dto.getDate() == null ) {
            throw new BadRequestException("The date cannot be null");
        }
    }

    private void mapToDtoEntity(EvaluationRequestDto evaluationDto, Evaluation evaluation) {
        evaluation.setTitle(evaluationDto.getTitle());
        evaluation.setDescription(evaluationDto.getDescription());
        evaluation.setMinValue(evaluationDto.getMinValue());
        evaluation.setMaxValue(evaluationDto.getMaxValue());
        evaluation.setDate(evaluationDto.getDate());
    }

    private EvaluationResponseDto convertToDto(Evaluation evaluation) {
        return EvaluationResponseDto.builder()
                .id(evaluation.getId())
                .title(evaluation.getTitle())
                .date(evaluation.getDate())
                .createdBy(evaluation.getCreatedBy().getUsername())
                .build();
    }

    private EvaluationDetailedResponseDto convertToDetailedDto(Evaluation evaluation) {
        return EvaluationDetailedResponseDto.builder()
                .id(evaluation.getId())
                .title(evaluation.getTitle())
                .description(evaluation.getDescription())
                .minValue(evaluation.getMinValue())
                .maxValue(evaluation.getMaxValue())
                .date(evaluation.getDate())
                .createdBy(evaluation.getCreatedBy().getUsername())
                .classrooms(evaluation.getClassrooms().stream()
                        .map(Classroom::getName)
                        .toList())
                .build();
    }

    private NoteResponseDto convertToNoteDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .value(note.getValue())
                .studentName(note.getUser().getFirstName() + " " + note.getUser().getLastName())
                .build();
    }
}
package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.*;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.Note;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.EvaluationService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final ClassroomRepository classroomRepository;
    private final NoteRepository noteRepository;

    @Override
    public ApiResponse getAllEvaluations() {
        List<Evaluation> evaluations = evaluationRepository.findAll();
        return ApiResponse.builder()
                .message("Liste des évaluations")
                .response(evaluations.stream().map(this::convertToDto).toList())
                .build();
    }

    @Override
    public ApiResponse getEvaluationById(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        return ApiResponse.builder()
                .message("Évaluation trouvée")
                .response(convertToDetailedDto(evaluation))
                .build();
    }

    @Override
    public ApiResponse createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé"));

        if (!teacher.isTeacher()) {
            throw new BadRequestException("Seuls les enseignants peuvent créer des évaluations");
        }

        validateEvaluationRequest(evaluationDto);

        Evaluation evaluation = new Evaluation();
        evaluation.setTitle(evaluationDto.getTitle());
        evaluation.setDescription(evaluationDto.getDescription());
        evaluation.setMinValue(evaluationDto.getMinValue());
        evaluation.setMaxValue(evaluationDto.getMaxValue());
        evaluation.setDate(evaluationDto.getDate());
        evaluation.setCreatedBy(teacher);

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);
        return ApiResponse.builder()
                .message("Évaluation créée avec succès")
                .response(convertToDto(savedEvaluation))
                .build();
    }

    @Override
    public ApiResponse updateEvaluation(Long id, EvaluationRequestDto evaluationDto) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        validateEvaluationRequest(evaluationDto);
        evaluation.setTitle(evaluationDto.getTitle());
        evaluation.setDescription(evaluationDto.getDescription());
        evaluation.setMinValue(evaluationDto.getMinValue());
        evaluation.setMaxValue(evaluationDto.getMaxValue());
        evaluation.setDate(evaluationDto.getDate());

        Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
        return ApiResponse.builder()
                .message("Évaluation mise à jour")
                .response(convertToDto(updatedEvaluation))
                .build();
    }

    @Override
    public ApiResponse deleteEvaluation(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        if (!evaluation.getNotes().isEmpty()) {
            throw new BadRequestException("Impossible de supprimer : l'évaluation a des notes associées");
        }

        evaluationRepository.delete(evaluation);
        return ApiResponse.builder()
                .message("Évaluation supprimée")
                .build();
    }

    @Override
    public ApiResponse addClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée"));

        if (evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("Cette classe est déjà associée à l'évaluation");
        }

        evaluation.getClassrooms().add(classroom);
        evaluationRepository.save(evaluation);

        return ApiResponse.builder()
                .message("Classe ajoutée à l'évaluation")
                .response(convertToDto(evaluation))
                .build();
    }

    @Override
    public ApiResponse getEvaluationResults(Long evaluationId) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        List<Note> notes = noteRepository.findByEvaluationId(evaluationId);

        EvaluationResultsDto results = new EvaluationResultsDto();
        results.setEvaluationTitle(evaluation.getTitle());
        results.setAverage(calculateAverage(notes));
        results.setNotes(notes.stream().map(this::convertToNoteDto).toList());

        return ApiResponse.builder()
                .message("Résultats de l'évaluation")
                .response(results)
                .build();
    }

    private void validateEvaluationRequest(EvaluationRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Le titre est obligatoire");
        }

        if (dto.getMinValue() < 0) {
            throw new BadRequestException("La note minimale ne peut pas être négative");
        }

        if (dto.getMaxValue() > 100) {
            throw new BadRequestException("La note maximale ne peut pas dépasser 100");
        }

        if (dto.getMinValue() >= dto.getMaxValue()) {
            throw new BadRequestException("La note minimale doit être inférieure à la note maximale");
        }

        if (dto.getDate() == null || dto.getDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("La date doit être dans le futur");
        }
    }

    private double calculateAverage(List<Note> notes) {
        return notes.stream()
                .mapToInt(Note::getValue)
                .average()
                .orElse(0.0);
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
                .studentName(note.getUser().getFirstname() + " " + note.getUser().getLastname())
                .build();
    }
}
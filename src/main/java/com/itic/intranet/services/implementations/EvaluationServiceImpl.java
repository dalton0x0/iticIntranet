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
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
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
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant non trouvé"));

        if (!user.isTeacher()) {
            throw new BadRequestException("Seuls les enseignants peuvent créer des évaluations");
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
            throw new BadRequestException("Impossible de supprimer : l'évaluation a des notes associées");
        }

        evaluationRepository.delete(evaluation);
    }

    @Override
    public Evaluation addClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation = getEvaluationById(evaluationId);

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Classe non trouvée"));

        if (evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("Cette classe est déjà associée à l'évaluation");
        }

        evaluation.getClassrooms().add(classroom);
        evaluationRepository.save(evaluation);

        return evaluation;
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
        if (dto.getDate() == null ) {
            throw new BadRequestException("La date ne peut pas être nulle");
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
                .studentName(note.getUser().getFirstname() + " " + note.getUser().getLastname())
                .build();
    }
}
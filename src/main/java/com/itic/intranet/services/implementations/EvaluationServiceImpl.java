package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.mappers.EvaluationMapper;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.Note;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final EvaluationMapper evaluationMapper;

    @Override
    public List<EvaluationDetailedResponseDto> getAllEvaluations() {
        return evaluationRepository.findAll()
                .stream()
                .map(evaluationMapper::convertToDetailedDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationResponseDto getEvaluationById(Long id) {
        Evaluation evaluation = getEvaluation(id);
        return evaluationMapper.convertEntityToResponseDto(evaluation);
    }

    @Override
    public List<EvaluationResponseDto> searchEvaluations(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestException("Search title cannot be empty");
        }
        return evaluationRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(evaluationMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationResponseDto createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        if (!user.isTeacher()) {
            throw new BadRequestException("Only teacher can create evaluations");
        }
        validateEvaluationRequest(evaluationDto);
        Evaluation evaluation = evaluationMapper.convertDtoToEntity(evaluationDto, user);
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);
        return evaluationMapper.convertEntityToResponseDto(savedEvaluation);
    }

    @Override
    public EvaluationResponseDto updateEvaluation(Long id, EvaluationRequestDto evaluationDto) {
        Evaluation existingEvaluation = getEvaluation(id);
        validateEvaluationRequest(evaluationDto);
        evaluationMapper.updateFromEntityDto(evaluationDto, existingEvaluation, existingEvaluation.getCreatedBy());
        Evaluation updatedEvaluation = evaluationRepository.save(existingEvaluation);
        return evaluationMapper.convertEntityToResponseDto(updatedEvaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        Evaluation evaluation = getEvaluation(id);
        if (!evaluation.getNotes().isEmpty()) {
            throw new BadRequestException("Cannot delete: Evaluation has associated notes");
        }
        evaluationRepository.delete(evaluation);
    }

    @Override
    public void finishEvaluation(Long id) {
        Evaluation evaluation = getEvaluation(id);
        if (evaluation.isFinished()) {
            throw new BadRequestException("Evaluation has already been finished");
        }
        evaluation.setFinished(true);
        evaluation.setFinishedAt(LocalDateTime.now());
        evaluationRepository.save(evaluation);
    }

    private Evaluation getEvaluation(Long id) {
        return evaluationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        );
    }

    private void validateEvaluationRequest(EvaluationRequestDto evaluationRequestDto) {
        if (evaluationRequestDto.getTitle() == null || evaluationRequestDto.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Title is required");
        }
        if (evaluationRequestDto.getMinValue() < 0) {
            throw new BadRequestException("The minimum note cannot be negative");
        }
        if (evaluationRequestDto.getMaxValue() > 100) {
            throw new BadRequestException("The maximum note cannot exceed 100");
        }
        if (evaluationRequestDto.getMinValue() >= evaluationRequestDto.getMaxValue()) {
            throw new BadRequestException("The minimum note must be lower than the maximum score");
        }
    }

    private NoteResponseDto convertToNoteDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .value(note.getValue())
                .studentName(note.getStudent().getFirstName() + " " + note.getStudent().getLastName())
                .build();
    }
}

package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.EvaluationMapper;
import com.itic.intranet.models.mysql.Evaluation;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.services.EvaluationService;
import com.itic.intranet.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationMapper evaluationMapper;
    private final EntityHelper entityHelper;
    private final LogService logService;

    @Override
    public List<EvaluationDetailedResponseDto> getAllEvaluations() {
        List<EvaluationDetailedResponseDto> allEvaluations = evaluationRepository.findAll()
                .stream()
                .map(evaluationMapper::convertToDetailedDto)
                .toList();
        logService.info(
                "SYSTEM",
                "GET_ALL_EVALUATIONS",
                "Getting all evaluations",
                Map.of(
                        "allEvaluationsCounted", allEvaluations.size()
                )
        );
        return allEvaluations;
    }

    @Override
    public EvaluationResponseDto getEvaluationById(Long id) {
        Evaluation evaluation = entityHelper.getEvaluation(id);
        logService.info(
                "SYSTEM",
                "GET_EVALUATION",
                "Getting evaluation by ID",
                Map.of(
                        "evaluationFound", evaluation.getTitle()
                )
        );
        return evaluationMapper.convertEntityToResponseDto(evaluation);
    }

    @Override
    public List<EvaluationResponseDto> searchEvaluations(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestException("Search title cannot be empty");
        }
        List<EvaluationResponseDto> results = evaluationRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(evaluationMapper::convertEntityToResponseDto)
                .toList();
        logService.info(
                "SYSTEM",
                "SEARCH_EVALUATION",
                "Searching evaluations",
                Map.of(
                        "keyword", title,
                        "evaluationsFoundedCount", results.size()
                )
        );
        return results;
    }

    @Override
    public EvaluationResponseDto createEvaluation(EvaluationRequestDto evaluationDto, Long teacherId) {
        User user = entityHelper.getUser(teacherId);
        if (!user.isTeacher()) {
            throw new BadRequestException("Only teacher can create evaluations");
        }
        validateEvaluationRequest(evaluationDto);
        Evaluation evaluation = evaluationMapper.convertDtoToEntity(evaluationDto, user);
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);
        logService.info(
                "SYSTEM",
                "CREATE_EVALUATION",
                "Creating new evaluation",
                Map.of(
                        "evaluationCreated", savedEvaluation.getTitle()
                )
        );
        return evaluationMapper.convertEntityToResponseDto(savedEvaluation);
    }

    @Override
    public EvaluationResponseDto updateEvaluation(Long id, EvaluationRequestDto evaluationDto) {
        Evaluation existingEvaluation = entityHelper.getEvaluation(id);
        validateEvaluationRequest(evaluationDto);
        evaluationMapper.updateFromEntityDto(evaluationDto, existingEvaluation, existingEvaluation.getCreatedBy());
        Evaluation updatedEvaluation = evaluationRepository.save(existingEvaluation);
        logService.info(
                "SYSTEM",
                "UPDATE_EVALUATION",
                "Update existing evaluation",
                Map.of(
                        "evaluationUpdated", updatedEvaluation.getTitle()
                )
        );
        return evaluationMapper.convertEntityToResponseDto(updatedEvaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        Evaluation evaluation = entityHelper.getEvaluation(id);
        if (!evaluation.getNotes().isEmpty()) {
            throw new BadRequestException("Cannot delete: Evaluation has associated notes");
        }
        evaluationRepository.delete(evaluation);
        logService.info(
                "SYSTEM",
                "DELETE_EVALUATION",
                "Deleting evaluation",
                Map.of(
                        "evaluationDeleted", evaluation.getTitle()
                )
        );
    }

    @Override
    public void finishEvaluation(Long id) {
        Evaluation evaluation = entityHelper.getEvaluation(id);
        if (evaluation.isFinished()) {
            throw new BadRequestException("Evaluation has already been finished");
        }
        evaluation.setFinished(true);
        evaluation.setFinishedAt(LocalDateTime.now());
        evaluationRepository.save(evaluation);
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
}

package com.itic.intranet.mappers;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.dtos.EvaluationRequestDto;
import com.itic.intranet.dtos.EvaluationResponseDto;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EvaluationMapper {
    public Evaluation convertDtoToEntity(EvaluationRequestDto evaluationDto, User user) {
        return Evaluation.builder()
                .id(evaluationDto.getId())
                .title(evaluationDto.getTitle().trim())
                .description(evaluationDto.getDescription().trim())
                .minValue(evaluationDto.getMinValue())
                .maxValue(evaluationDto.getMaxValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .createdBy(user)
                .finished(false)
                .finishedAt(evaluationDto.getFinishedAt())
                .build();
    }

    public EvaluationResponseDto convertEntityToResponseDto(Evaluation evaluation) {
        return EvaluationResponseDto.builder()
                .id(evaluation.getId())
                .title(evaluation.getTitle())
                .createdAt(evaluation.getCreatedAt())
                .updatedAt(evaluation.getUpdatedAt())
                .createdBy(evaluation.getCreatedBy().getLastName() + " " + evaluation.getCreatedBy().getFirstName())
                .finished(evaluation.isFinished())
                .finishedAt(evaluation.getFinishedAt())
                .build();
    }

    public void updateFromEntityDto(EvaluationRequestDto evaluationRequestDto, Evaluation evaluation, User user) {
        evaluation.setTitle(evaluationRequestDto.getTitle().trim());
        evaluation.setDescription(evaluationRequestDto.getDescription().trim());
        evaluation.setMinValue(evaluationRequestDto.getMinValue());
        evaluation.setMaxValue(evaluationRequestDto.getMaxValue());
        evaluation.setUpdatedAt(LocalDateTime.now());
        evaluation.setCreatedBy(user);
    }

    public EvaluationDetailedResponseDto convertToDetailedDto(Evaluation evaluation) {
        return EvaluationDetailedResponseDto.builder()
                .id(evaluation.getId())
                .title(evaluation.getTitle())
                .description(evaluation.getDescription())
                .minValue(evaluation.getMinValue())
                .maxValue(evaluation.getMaxValue())
                .createdAt(evaluation.getCreatedAt())
                .updatedAt(evaluation.getUpdatedAt())
                .createdBy(evaluation.getCreatedBy().getLastName() + " " + evaluation.getCreatedBy().getFirstName())
                .finished(evaluation.isFinished())
                .finishedAt(evaluation.getFinishedAt())
                .classrooms(evaluation.getClassrooms().stream()
                        .map(Classroom::getName)
                        .toList())
                .build();
    }
}

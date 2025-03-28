package com.itic.intranet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationResultsDto {
    private String evaluationTitle;
    private double average;
    private List<NoteResponseDto> notes;
}
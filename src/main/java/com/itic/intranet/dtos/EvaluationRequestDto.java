package com.itic.intranet.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EvaluationRequestDto {
    private Long id;
    private String title;
    private String description;
    private int minValue;
    private int maxValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private LocalDateTime finishedAt;
}

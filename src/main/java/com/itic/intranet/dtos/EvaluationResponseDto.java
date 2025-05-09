package com.itic.intranet.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EvaluationResponseDto {
    private Long id;
    private String title;
    private LocalDateTime date;
    private String createdBy;
}

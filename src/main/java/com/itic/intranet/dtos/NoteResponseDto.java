package com.itic.intranet.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteResponseDto {
    private Long id;
    private int value;
    private Long studentId;
    private String studentName;
    private Long evaluationId;
    private String evaluationTitle;
}
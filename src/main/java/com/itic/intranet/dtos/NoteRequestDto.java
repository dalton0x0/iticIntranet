package com.itic.intranet.dtos;

import lombok.Data;

@Data
public class NoteRequestDto {
    private Long studentId;
    private Long evaluationId;
    private int value;
}
package com.itic.intranet.dtos;

import lombok.Data;

@Data
public class NoteRequestDto {
    private Long id;
    private int value;
    private Long studentId;
    private Long evaluationId;
}

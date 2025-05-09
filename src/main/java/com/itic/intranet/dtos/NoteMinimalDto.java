package com.itic.intranet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteMinimalDto {
    private Long id;
    private int value;
    private String evaluationTitle;
}

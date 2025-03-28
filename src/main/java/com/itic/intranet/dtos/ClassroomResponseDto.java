package com.itic.intranet.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomResponseDto {
    private Long id;
    private String name;
}
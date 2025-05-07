package com.itic.intranet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDetailedResponseDto {
    private Long id;
    private String name;
    private List<UserMinimalDto> students;
}

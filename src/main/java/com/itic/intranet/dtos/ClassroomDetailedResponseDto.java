package com.itic.intranet.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ClassroomDetailedResponseDto {
    private Long id;
    private String name;
    private List<UserMinimalDto> students;
}
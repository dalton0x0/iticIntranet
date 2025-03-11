package com.itic.intranet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassroomRequestDto {
    @NotNull(message = "Name can not be null !")
    @NotBlank(message = "Name is empty !")
    private String name;
}

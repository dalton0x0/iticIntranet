package com.itic.intranet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequestDto {
    @NotNull(message = "Wording can not be null !")
    @NotBlank(message = "Wording is empty !")
    private String wording;
}

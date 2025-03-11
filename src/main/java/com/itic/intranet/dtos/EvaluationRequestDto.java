package com.itic.intranet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EvaluationRequestDto {
    @NotNull(message = "Title can not be null !")
    @NotBlank(message = "Title is empty !")
    private String title;
    @NotNull(message = "Description can not be null !")
    @NotBlank(message = "Description is empty !")
    private String description;
    @NotNull(message = "Minimum value can not be null !")
    private int minValue;
    @NotNull(message = "Maximum value can not be null !")
    private int maxValue;
    private Date date;
    @NotNull
    @NotEmpty
    private List<Long> user_id;
}

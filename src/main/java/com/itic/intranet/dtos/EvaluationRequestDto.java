package com.itic.intranet.dtos;

import com.itic.intranet.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationRequestDto {
    private String title;
    private String description;
    private int minValue;
    private int maxValue;
    private Date date;
    private List<User> users;
}

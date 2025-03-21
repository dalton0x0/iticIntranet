package com.itic.intranet.dtos;

import com.itic.intranet.models.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EvaluationRequestDto {
    private String title;
    private String description;
    private int minValue;
    private int maxValue;
    private Date date;
    private List<User> users;
}

package com.itic.intranet.dtos;

import com.itic.intranet.models.User;
import lombok.Data;

import java.util.List;

@Data
public class ClassroomRequestDto {
    private String name;
    private List<User> users;
}

package com.itic.intranet.dtos;

import com.itic.intranet.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomRequestDto {
    private String name;
    private List<User> users;
}

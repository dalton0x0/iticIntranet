package com.itic.intranet.dtos;

import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserRequestDto {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private Role role;
    private List<Classroom> classroom;
}

package com.itic.intranet.dtos;

import com.itic.intranet.enums.RoleType;
import lombok.Data;

@Data
public class UserRequestDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private RoleType roleType;
}

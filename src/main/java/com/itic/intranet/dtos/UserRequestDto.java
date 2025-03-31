package com.itic.intranet.dtos;

import com.itic.intranet.enums.RoleType;
import lombok.Data;

@Data
public class UserRequestDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private RoleType roleType;
}
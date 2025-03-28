package com.itic.intranet.dtos;

import com.itic.intranet.enums.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private RoleType roleType;
    private boolean active;
}
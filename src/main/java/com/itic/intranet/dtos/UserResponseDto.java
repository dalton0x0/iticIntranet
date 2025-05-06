package com.itic.intranet.dtos;

import com.itic.intranet.enums.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private RoleType roleType;
    private boolean active;
}

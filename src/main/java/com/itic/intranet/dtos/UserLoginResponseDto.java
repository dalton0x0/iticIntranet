package com.itic.intranet.dtos;

import com.itic.intranet.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponseDto {
    private Long userId;
    private String username;
    private RoleType roleType;
}

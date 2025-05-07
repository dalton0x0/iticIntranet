package com.itic.intranet.dtos;

import com.itic.intranet.enums.RoleType;
import lombok.Data;

@Data
public class RoleRequestDto {
    private Long id;
    private RoleType roleType;
    private String label;
}

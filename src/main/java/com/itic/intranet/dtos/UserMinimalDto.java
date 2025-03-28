package com.itic.intranet.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMinimalDto {
    private Long id;
    private String firstname;
    private String lastname;
}
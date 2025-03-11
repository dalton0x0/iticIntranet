package com.itic.intranet.dtos;

import lombok.Data;

@Data
public class NoteRequestDto {
    private int value;
    private Long user_id;
    private Long evaluation_id;
}

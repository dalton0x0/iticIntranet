package com.itic.intranet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogDto {
    private String id;
    private String level;
    private String actor;
    private String action;
    private String message;
    private Map<String, Object> details;
    private LocalDateTime timestamp;
}

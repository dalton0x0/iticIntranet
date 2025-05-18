package com.itic.intranet.dtos;

import com.itic.intranet.models.mongo.Log;
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

    public static LogDto from(Log log) {
        return LogDto.builder()
                .id(log.getId())
                .level(log.getLevel())
                .actor(log.getActor())
                .action(log.getAction())
                .message(log.getMessage())
                .details(log.getDetails())
                .timestamp(log.getTimestamp())
                .build();
    }
}

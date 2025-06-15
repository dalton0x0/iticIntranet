package com.itic.intranet.mappers;

import com.itic.intranet.dtos.LogDto;
import com.itic.intranet.models.mongo.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogMapper {

    public LogDto convertToDto(Log log) {
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

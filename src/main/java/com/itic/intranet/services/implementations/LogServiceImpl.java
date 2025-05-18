package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.LogDto;
import com.itic.intranet.models.mongo.Log;
import com.itic.intranet.repositories.LogRepository;
import com.itic.intranet.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public void info(String actor, String action, String message, Map<String, Object> details) {
        saveLog("INFO", actor, action, message, details);
    }

    @Override
    public void warn(String actor, String action, String message, Map<String, Object> details) {
        saveLog("WARN", actor, action, message, details);
    }

    @Override
    public void error(String actor, String action, String message, Map<String, Object> details) {
        saveLog("ERROR", actor, action, message, details);
    }

    @Override
    public List<LogDto> getAllLogs() {
        return logRepository.findAll()
                .stream()
                .map(LogDto::from)
                .toList();
    }

    private void saveLog(String level, String actor, String action, String message, Map<String, Object> details) {
        logRepository.save(Log.builder()
                .level(level)
                .actor(actor)
                .action(action)
                .message(message)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build());
    }
}

package com.itic.intranet.services;

import com.itic.intranet.dtos.LogDto;

import java.util.List;
import java.util.Map;

public interface LogService {
    void info(String actor, String action, String message, Map<String, Object> details);
    void warn(String actor, String action, String message, Map<String, Object> details);
    void error(String actor, String action, String message, Map<String, Object> details);
    List<LogDto> getAllLogs();
}

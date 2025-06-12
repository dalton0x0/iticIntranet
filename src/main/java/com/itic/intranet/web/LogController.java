package com.itic.intranet.web;

import com.itic.intranet.dtos.LogDto;
import com.itic.intranet.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v18/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<LogDto>> getAllLogs() {
        List<LogDto> logs = logService.getAllLogs();
        return logs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(logs);
    }

    @GetMapping("/{actor}")
    public ResponseEntity<List<LogDto>> getLogsOfActor(@PathVariable String actor) {
        List<LogDto> actorLogs = logService.getLogsByActor(actor);
        return actorLogs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(actorLogs);
    }
}

package com.itic.intranet.exceptions;

import com.itic.intranet.services.LogService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogService logService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundHandler(ResourceNotFoundException exception) {
        ApiResponse error = ApiResponse.builder()
                .message(exception.getMessage())
                .build();
        logService.warn("SYSTEM", "RESOURCE_NOT_FOUND", exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> badRequestHandler(BadRequestException exception) {
        ApiResponse error = ApiResponse.builder()
                .message("Error: " + exception.getMessage())
                .build();
        logService.warn("SYSTEM", "BAD_REQUEST", exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> runtimeExceptionHandler(RuntimeException exception) {
        ApiResponse error = ApiResponse.builder()
                .message("Error: " + exception.getMessage())
                .build();
        logService.error("SYSTEM", "RUNTIME_EXCEPTION", exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

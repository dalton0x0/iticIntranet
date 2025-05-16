package com.itic.intranet.web;

import com.itic.intranet.dtos.UserLoginRequestDto;
import com.itic.intranet.dtos.UserLoginResponseDto;
import com.itic.intranet.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v18/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto ) {
        return ResponseEntity.ok(authService.login(userLoginRequestDto));
    }
}

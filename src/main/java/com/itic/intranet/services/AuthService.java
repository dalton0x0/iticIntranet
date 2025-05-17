package com.itic.intranet.services;

import com.itic.intranet.dtos.LoginRequestDto;
import com.itic.intranet.dtos.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto);
}

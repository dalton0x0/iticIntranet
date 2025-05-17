package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.LoginRequestDto;
import com.itic.intranet.dtos.LoginResponseDto;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.AuthService;
import com.itic.intranet.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {

        var existingUser = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwtToken = jwtService.generateJwtToken(existingUser);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Access-Control-Expose-Headers", "Authorization");
        responseHeaders.add("Authorization", "Bearer " + jwtToken);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(LoginResponseDto.builder()
                        .message("User logged successfully !")
                        .build());
    }
}

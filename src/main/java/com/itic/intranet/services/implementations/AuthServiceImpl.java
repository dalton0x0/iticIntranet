package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.LoginRequestDto;
import com.itic.intranet.dtos.LoginResponseDto;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.AuthService;
import com.itic.intranet.services.JwtService;
import com.itic.intranet.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final LogService logService;

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {

        var existingUser = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> {
                    logService.warn(
                            loginRequestDto.getUsername(),
                            "LOGIN",
                            "Login failed ! Need to connect with invalid username",
                            Map.of(
                                    "username", loginRequestDto.getUsername()
                            )
                    );
                    return new UsernameNotFoundException("Invalid username or password");
                });

        try {
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

            logService.info(
                    loginRequestDto.getUsername(),
                    "LOGIN",
                    "Login successful",
                    Map.of(
                            "userId", existingUser.getId(),
                            "userName", existingUser.getFullName(),
                            "role", existingUser.getRole().getRoleType().name()
                    )
            );

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(LoginResponseDto.builder()
                            .message("User logged successfully !")
                            .build());
        } catch (Exception e) {
            logService.warn(
                    loginRequestDto.getUsername(),
                    "LOGIN",
                    "Login failed",
                    Map.of(
                            "userId", existingUser.getId(),
                            "userName", existingUser.getFullName(),
                            "role", existingUser.getRole().getRoleType().name()
                    )
            );
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}

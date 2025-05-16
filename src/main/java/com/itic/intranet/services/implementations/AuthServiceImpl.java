package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserLoginRequestDto;
import com.itic.intranet.dtos.UserLoginResponseDto;
import com.itic.intranet.models.User;
import com.itic.intranet.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.getUsername(),
                        userLoginRequestDto.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();

        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roleType(user.getRole().getRoleType())
                .build();
    }
}

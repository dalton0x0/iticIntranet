package com.itic.intranet.services;

import com.itic.intranet.dtos.UserLoginRequestDto;
import com.itic.intranet.dtos.UserLoginResponseDto;

public interface AuthService {
    UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto);
}

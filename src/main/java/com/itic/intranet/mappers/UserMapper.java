package com.itic.intranet.mappers;

import com.itic.intranet.dtos.UserMinimalDto;
import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.models.mysql.Role;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.security.CustomPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final CustomPasswordEncoder passwordEncoder;

    public User convertDtoToEntity(UserRequestDto userDto, Role role) {
        return User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName().trim())
                .lastName(userDto.getLastName().trim())
                .email(userDto.getEmail().trim().toLowerCase())
                .username(userDto.getUsername().trim())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(role)
                .active(true)
                .taughtClassrooms(new ArrayList<>())
                .notes(new ArrayList<>())
                .build();
    }

    public UserResponseDto convertEntityToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .roleType(user.getRole().getRoleType())
                .active(user.isActive())
                .build();
    }

    public void updateEntityFromDto(UserRequestDto userDto, User user, Role role) {
        user.setFirstName(userDto.getFirstName().trim());
        user.setLastName(userDto.getLastName().trim());
        user.setEmail(userDto.getEmail().trim().toLowerCase());
        user.setUsername(userDto.getUsername().trim());
        user.setPassword(user.getPassword());
        user.setRole(role);
    }

    public UserMinimalDto convertEntityToUserMinimalDto(User user) {
        return new UserMinimalDto(user.getId(), user.getFirstName(), user.getLastName());
    }
}

package com.itic.intranet.services.implementations;

import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.RolePropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolePropertyServiceImpl implements RolePropertyService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EntityHelper entityHelper;

    @Override
    public List<UserResponseDto> getUsersOfRole(Long roleId) {
        entityHelper.getRole(roleId);
        List<User> users = userRepository.findByRole_Id(roleId);
        return users.stream()
                .map(userMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }
}

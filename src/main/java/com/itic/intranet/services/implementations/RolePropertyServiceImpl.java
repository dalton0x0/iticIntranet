package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.RoleRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.RolePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePropertyServiceImpl implements RolePropertyService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserResponseDto> getUsersOfRole(Long roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found");
        }
        List<User> users = userRepository.findByRole_Id(roleId);
        return users.stream()
                .map(userMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }
}

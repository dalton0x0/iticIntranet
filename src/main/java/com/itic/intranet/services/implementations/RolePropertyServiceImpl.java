package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.UserResponseDto;
import com.itic.intranet.enums.LogActor;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.UserMapper;
import com.itic.intranet.models.mysql.Role;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.LogService;
import com.itic.intranet.services.RolePropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RolePropertyServiceImpl implements RolePropertyService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EntityHelper entityHelper;
    private final LogService logService;

    @Override
    public List<UserResponseDto> getRoleUsers(Long roleId) {
        Role role = entityHelper.getRole(roleId);
        List<UserResponseDto> usersRole = userRepository.findByRole_Id(roleId)
                .stream()
                .map(userMapper::convertEntityToResponseDto)
                .toList();
        logService.info(
                LogActor.SYSTEM.name(),
                "GET_USERS_OF_ROLE",
                "Getting users of role",
                Map.of(
                        "roleType", role.getRoleType(),
                        "allUsersCounted", usersRole.size()
                )
        );
        return usersRole;
    }
}

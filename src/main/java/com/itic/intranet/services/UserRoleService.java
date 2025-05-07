package com.itic.intranet.services;

import com.itic.intranet.enums.RoleType;

public interface UserRoleService {
    void assignRoleToUser(Long userId, Long roleId);
    void removeRoleFromUser(Long userId);
    RoleType getUserRoleType(Long userId);
}

package com.itic.intranet.services;

import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role getRoleByWording(String wording);
    Role addRole(RoleRequestDto roleDto);
    Role updateRole(Long id, RoleRequestDto roleDto);
    void deleteRole(Long id);
}

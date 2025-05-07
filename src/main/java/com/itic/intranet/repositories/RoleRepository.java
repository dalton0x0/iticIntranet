package com.itic.intranet.repositories;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(RoleType roleType);
    Optional<Role> findByLabel(String label);
}

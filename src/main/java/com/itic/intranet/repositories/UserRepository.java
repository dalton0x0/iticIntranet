package com.itic.intranet.repositories;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByActive(boolean active);
    List<User> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String firstname, String lastname);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.id = :roleId")
    long countByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT u FROM User u WHERE u.classroom.id = :classroomId AND u.role.roleType = 'STUDENT'")
    List<User> findByClassroomIdAndRoleType(@Param("classroomId") Long classroomId, @Param("roleType") RoleType roleType);

    @Query("SELECT COUNT(u) FROM User u WHERE u.classroom.id = :classroomId AND u.role.roleType = 'STUDENT'")
    long countByClassroomIdAndRoleType(@Param("classroomId") Long classroomId, @Param("roleType") RoleType roleType);

    @Query("SELECT DISTINCT t FROM User t JOIN t.taughtClassrooms c WHERE c.id = :classroomId")
    List<User> findTeachersByClassroomId(@Param("classroomId") Long classroomId);
}
package com.itic.intranet.repositories;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findByRole_Id(Long roleId);
    List<User> findByActive(boolean active);
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstname, String lastname);

    @Query("SELECT u FROM User u WHERE u.classroom.id = :classroomId AND u.role.roleType = 'STUDENT'")
    List<User> findByClassroomIdAndRoleType(@Param("classroomId") Long classroomId, @Param("roleType") RoleType roleType);

    @Query("SELECT DISTINCT t FROM User t JOIN t.taughtClassrooms c WHERE c.id = :classroomId")
    List<User> findTeachersByClassroomId(@Param("classroomId") Long classroomId);

    long countByRole_Id(Long roleId);

    @Query("SELECT COUNT(u) FROM User u WHERE u.classroom.id = :classroomId AND u.role.roleType = 'STUDENT'")
    long countByClassroomIdAndRoleType(@Param("classroomId") Long classroomId, @Param("roleType") RoleType roleType);
}

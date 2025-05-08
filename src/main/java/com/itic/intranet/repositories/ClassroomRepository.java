package com.itic.intranet.repositories;

import com.itic.intranet.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByName(String name);
    Optional<Classroom> findByName(String name);
    List<Classroom> findByNameContaining(String name);
}

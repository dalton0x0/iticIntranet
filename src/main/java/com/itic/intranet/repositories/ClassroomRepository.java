package com.itic.intranet.repositories;

import com.itic.intranet.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByNameContaining(String name);
}

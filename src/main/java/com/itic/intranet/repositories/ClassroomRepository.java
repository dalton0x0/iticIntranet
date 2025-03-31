package com.itic.intranet.repositories;

import com.itic.intranet.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findByNameContaining(String name);
    boolean existsByName(String name);
}

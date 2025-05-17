package com.itic.intranet.repositories;

import com.itic.intranet.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT e FROM Evaluation e JOIN e.classrooms c WHERE c.id = :classroomId")
    List<Evaluation> findEvaluationByClassroomsId(@Param("classroomId") Long classroomId);
}

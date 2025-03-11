package com.itic.intranet.repositories;

import com.itic.intranet.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByTitleContaining(String title);
}

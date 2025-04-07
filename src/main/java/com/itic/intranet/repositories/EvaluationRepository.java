package com.itic.intranet.repositories;

import com.itic.intranet.dtos.EvaluationDetailedResponseDto;
import com.itic.intranet.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByTitleContainingIgnoreCase(String title);
}
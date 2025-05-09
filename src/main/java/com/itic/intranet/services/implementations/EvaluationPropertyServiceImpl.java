package com.itic.intranet.services.implementations;

import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Classroom;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.repositories.ClassroomRepository;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.services.EvaluationPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationPropertyServiceImpl implements EvaluationPropertyService {

    private final EvaluationRepository evaluationRepository;
    private final ClassroomRepository classroomRepository;

    @Override
    public void addClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation=  evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        );
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ResourceNotFoundException("Classroom not found"));

        if (evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("This evaluation has already associated to this classrooms");
        }

        evaluation.getClassrooms().add(classroom);
        evaluationRepository.save(evaluation);
    }

    @Override
    public void removeClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation=  evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new ResourceNotFoundException("Evaluation not found")
        );
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ResourceNotFoundException("Classroom not found"));

        if (!evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("This evaluation has been already removed to this classroom");
        }

        evaluation.getClassrooms().remove(classroom);
        evaluationRepository.save(evaluation);
    }
}

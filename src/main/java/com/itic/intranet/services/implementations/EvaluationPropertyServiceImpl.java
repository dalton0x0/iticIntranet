package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.NoteMapper;
import com.itic.intranet.models.mysql.Classroom;
import com.itic.intranet.models.mysql.Evaluation;
import com.itic.intranet.models.mysql.Note;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.services.EvaluationPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationPropertyServiceImpl implements EvaluationPropertyService {

    private final EvaluationRepository evaluationRepository;
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final EntityHelper entityHelper;

    @Override
    public void addClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation = entityHelper.getEvaluation(evaluationId);
        Classroom classroom = entityHelper.getClassroom(classroomId);
        if (evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("This evaluation has already associated to this classrooms");
        }
        evaluation.getClassrooms().add(classroom);
        evaluationRepository.save(evaluation);
    }

    @Override
    public void removeClassroomToEvaluation(Long evaluationId, Long classroomId) {
        Evaluation evaluation = entityHelper.getEvaluation(evaluationId);
        Classroom classroom = entityHelper.getClassroom(classroomId);
        if (!evaluation.getClassrooms().contains(classroom)) {
            throw new BadRequestException("This evaluation has been already removed to this classroom");
        }
        evaluation.getClassrooms().remove(classroom);
        evaluationRepository.save(evaluation);
    }

    @Override
    public List<NoteResponseDto> getEvaluationNotes(Long evaluationId) {
        List<Note> notes = noteRepository.findByEvaluationId(evaluationId);
        return notes.stream()
                .map(noteMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }
}

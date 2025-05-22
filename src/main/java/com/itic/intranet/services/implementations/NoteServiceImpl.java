package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteMinimalDto;
import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.NoteMapper;
import com.itic.intranet.models.mysql.Evaluation;
import com.itic.intranet.models.mysql.Note;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final EntityHelper entityHelper;

    @Override
    public List<NoteResponseDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream()
                .map(noteMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponseDto getNoteById(Long id) {
        Note note = entityHelper.getNote(id);
        return noteMapper.convertEntityToResponseDto(note);
    }

    @Override
    public NoteResponseDto createNote(NoteRequestDto noteDto) {
        validateNoteRequest(noteDto);
        User student = entityHelper.getUser(noteDto.getStudentId());
        if (!student.isStudent()) {
            throw new BadRequestException("Only students can have notes");
        }
        Evaluation evaluation = entityHelper.getEvaluation(noteDto.getEvaluationId());
        if (!isStudentInEvaluationClass(student, evaluation)) {
            throw new BadRequestException("Student is not in the evaluation class");
        }
        if (evaluation.getNotes().stream().anyMatch(note -> note.getStudent().getId().equals(student.getId()))) {
            throw new BadRequestException("The student already has a note for this evaluation");
        }
        Note note = noteMapper.convertDtoToEntity(noteDto, student, evaluation);
        Note savedNote = noteRepository.save(note);
        return noteMapper.convertEntityToResponseDto(savedNote);
    }

    @Override
    public NoteResponseDto updateNote(Long id, NoteRequestDto noteDto) {
        Note existingNote = entityHelper.getNote(id);
        User student = entityHelper.getUser(noteDto.getStudentId());
        Evaluation evaluation = entityHelper.getEvaluation(noteDto.getEvaluationId());
        validateNoteRequest(noteDto);
        noteMapper.updateEntityFromDto(noteDto, existingNote, student, evaluation);
        return noteMapper.convertEntityToResponseDto(noteRepository.save(existingNote));
    }

    @Override
    public void deleteNote(Long id) {
        Note note = entityHelper.getNote(id);
        noteRepository.delete(note);
    }

    @Override
    public List<NoteResponseDto> getNotesByEvaluation(Long evaluationId) {
        List<Note> notes = noteRepository.findByEvaluationId(evaluationId);
        return notes.stream()
                .map(noteMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    private void validateNoteRequest(NoteRequestDto noteDto) {
        if (noteDto == null) {
            throw new BadRequestException("Note cannot be null");
        }
        if (noteDto.getStudentId() == null) {
            throw new BadRequestException("Student ID is required");
        }
        if (noteDto.getEvaluationId() == null) {
            throw new BadRequestException("Evaluation ID is required");
        }
        Evaluation evaluation = entityHelper.getEvaluation(noteDto.getEvaluationId());
        if (noteDto.getValue() < evaluation.getMinValue()) {
            throw new BadRequestException("Note cannot be less than " + evaluation.getMinValue());
        }
        if (noteDto.getValue() > evaluation.getMaxValue()) {
            throw new BadRequestException("Note cannot be more than " + evaluation.getMinValue());
        }
        if (evaluation.isFinished()) {
            throw new BadRequestException("Cannot create for this evaluation. It has already been finished");
        }
    }

    private boolean isStudentInEvaluationClass(User student, Evaluation evaluation) {
        if (student.getClassroom() == null) return false;
        return evaluation.getClassrooms().contains(student.getClassroom());
    }
}

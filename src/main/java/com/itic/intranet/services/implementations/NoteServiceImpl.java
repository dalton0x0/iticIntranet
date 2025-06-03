package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.helpers.EntityHelper;
import com.itic.intranet.mappers.NoteMapper;
import com.itic.intranet.models.mysql.Evaluation;
import com.itic.intranet.models.mysql.Note;
import com.itic.intranet.models.mysql.User;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.services.LogService;
import com.itic.intranet.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final EntityHelper entityHelper;
    private final LogService logService;

    @Override
    public List<NoteResponseDto> getAllNotes() {
        List<NoteResponseDto> allNotes = noteRepository.findAll()
                .stream()
                .map(noteMapper::convertEntityToResponseDto)
                .toList();
        logService.info(
                "SYSTEM",
                "GET_ALL_NOTES",
                "Getting all allNotes",
                Map.of(
                        "resultCount", allNotes.size()
                )
        );
        return allNotes;
    }

    @Override
    public NoteResponseDto getNoteById(Long id) {
        Note note = entityHelper.getNote(id);
        logService.info(
                "SYSTEM",
                "GET_NOTE",
                "Getting note by ID",
                Map.of(
                        "noteId", note.getId(),
                        "noteValue", note.getValue()
                )
        );
        return noteMapper.convertEntityToResponseDto(note);
    }

    @Override
    public NoteResponseDto addNote(NoteRequestDto noteDto) {
        validateNoteRequest(noteDto);
        User student = ensureStudent(noteDto);
        Evaluation evaluation = getEvaluation(noteDto, student);
        checkIfAlreadyNoted(evaluation, student);
        Note note = noteMapper.convertDtoToEntity(noteDto, student, evaluation);
        Note savedNote = noteRepository.save(note);
        logService.info(
                "SYSTEM",
                "ADD_NOTE",
                "Adding new note",
                Map.of(
                        "noteValue", savedNote.getValue(),
                        "evaluationId", savedNote.getEvaluation().getId(),
                        "evaluationNoted", savedNote.getEvaluation().getTitle(),
                        "studentId", savedNote.getStudent().getId(),
                        "studentNoted", savedNote.getStudent().getFullName()
                )
        );
        return noteMapper.convertEntityToResponseDto(savedNote);
    }

    @Override
    public NoteResponseDto updateNote(Long id, NoteRequestDto noteDto) {
        Note existingNote = entityHelper.getNote(id);
        User student = entityHelper.getUser(noteDto.getStudentId());
        Evaluation evaluation = entityHelper.getEvaluation(noteDto.getEvaluationId());
        validateNoteRequest(noteDto);
        noteMapper.updateEntityFromDto(noteDto, existingNote, student, evaluation);
        Note updatedNote = noteRepository.save(existingNote);
        logService.info(
                "SYSTEM",
                "UPDATE_NOTE",
                "Updating note",
                Map.of(
                        "noteValue", updatedNote.getValue(),
                        "evaluationId", updatedNote.getEvaluation().getId(),
                        "evaluationNoted", updatedNote.getEvaluation().getTitle(),
                        "studentId", updatedNote.getStudent().getId(),
                        "studentNoted", updatedNote.getStudent().getFullName()
                )
        );
        return noteMapper.convertEntityToResponseDto(updatedNote);
    }

    @Override
    public void deleteNote(Long id) {
        Note note = entityHelper.getNote(id);
        noteRepository.delete(note);
        logService.info(
                "SYSTEM",
                "DELETE_NOTE",
                "Deleting note",
                Map.of(
                        "noteId", note.getId(),
                        "noteValue", note.getValue()
                )
        );
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

    private User ensureStudent(NoteRequestDto noteDto) {
        User student = entityHelper.getUser(noteDto.getStudentId());
        if (!student.isStudent()) {
            throw new BadRequestException("Only students can have notes");
        }
        return student;
    }

    private Evaluation getEvaluation(NoteRequestDto noteDto, User student) {
        Evaluation evaluation = entityHelper.getEvaluation(noteDto.getEvaluationId());
        if (!isStudentInEvaluationClass(student, evaluation)) {
            throw new BadRequestException("Student is not in the evaluation class");
        }
        return evaluation;
    }

    private void checkIfAlreadyNoted(Evaluation evaluation, User student) {
        if (evaluation.getNotes().stream().anyMatch(note -> note.getStudent().getId().equals(student.getId()))) {
            throw new BadRequestException("The student already has a note for this evaluation");
        }
    }
}

package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteMinimalDto;
import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.mappers.NoteMapper;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.Note;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private NoteMapper noteMapper;

    @Override
    public List<NoteResponseDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream()
                .map(noteMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponseDto getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        return noteMapper.convertEntityToResponseDto(note);
    }

    @Override
    public NoteResponseDto createNote(NoteRequestDto noteDto) {
        validateNoteRequest(noteDto);
        User student = userRepository.findById(noteDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (!student.isStudent()) {
            throw new BadRequestException("Only students can have notes");
        }
        Evaluation evaluation = evaluationRepository.findById(noteDto.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found"));
        if (!isStudentInEvaluationClass(student, evaluation)) {
            throw new BadRequestException("Student is not in the evaluation class");
        }
        Note note = noteMapper.convertDtoToEntity(noteDto, student, evaluation);
        Note savedNote = noteRepository.save(note);
        return noteMapper.convertEntityToResponseDto(savedNote);
    }

    @Override
    public NoteResponseDto updateNote(Long id, NoteRequestDto noteDto) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        User student = userRepository.findById(noteDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Evaluation evaluation = evaluationRepository.findById(noteDto.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found"));
        validateNoteRequest(noteDto);
        noteMapper.updateEntityFromDto(noteDto, existingNote, student, evaluation);
        return noteMapper.convertEntityToResponseDto(noteRepository.save(existingNote));
    }

    @Override
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        noteRepository.delete(note);
    }

    @Override
    public List<NoteResponseDto> getNotesByEvaluation(Long evaluationId) {
        List<Note> notes = noteRepository.findByEvaluationId(evaluationId);
        return notes.stream()
                .map(noteMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteMinimalDto> getNotesByStudent(Long studentId) {
        List<Note> notes = noteRepository.findByStudentId(studentId);
        return notes.stream()
                .map(noteMapper::convertEntityToUserMinimalDto)
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
        Evaluation evaluation = evaluationRepository.findById(noteDto.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found"));
        if (noteDto.getValue() < evaluation.getMinValue()) {
            throw new BadRequestException("Note cannot be less than " + evaluation.getMinValue());
        }
        if (noteDto.getValue() > evaluation.getMaxValue()) {
            throw new BadRequestException("Note cannot be more than " + evaluation.getMinValue());
        }
    }

    private boolean isStudentInEvaluationClass(User student, Evaluation evaluation) {
        if (student.getClassroom() == null) return false;
        return evaluation.getClassrooms().contains(student.getClassroom());
    }
}

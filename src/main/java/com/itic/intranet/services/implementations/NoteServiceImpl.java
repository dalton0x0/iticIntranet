package com.itic.intranet.services.implementations;

import com.itic.intranet.dtos.NoteRequestDto;
import com.itic.intranet.dtos.NoteResponseDto;
import com.itic.intranet.exceptions.BadRequestException;
import com.itic.intranet.exceptions.ResourceNotFoundException;
import com.itic.intranet.models.Evaluation;
import com.itic.intranet.models.Note;
import com.itic.intranet.models.User;
import com.itic.intranet.repositories.EvaluationRepository;
import com.itic.intranet.repositories.NoteRepository;
import com.itic.intranet.repositories.UserRepository;
import com.itic.intranet.services.NoteService;
import com.itic.intranet.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EvaluationRepository evaluationRepository;

    @Override
    public List<NoteResponseDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream().map(this::convertToDto).toList();
    }

    @Override
    public NoteResponseDto getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée"));
        return convertToDto(note);
    }

    @Override
    public NoteResponseDto createNote(NoteRequestDto noteDto) {
        if (noteDto == null) {
            throw new BadRequestException("Les données de la note sont requises");
        }
        validateNoteRequest(noteDto);

        User student = userRepository.findById(noteDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        if (student.isTeacher()) {
            throw new BadRequestException("Seuls les étudiants peuvent recevoir des notes");
        }

        Evaluation evaluation = evaluationRepository.findById(noteDto.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        var note = Note.builder()
                .value(noteDto.getValue())
                .user(student)
                .evaluation(evaluation)
                .build();

        Note savedNote = noteRepository.save(note);
        return convertToDto(savedNote);
    }

    @Override
    public NoteResponseDto updateNote(Long id, NoteRequestDto noteDto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée"));

        validateNoteRequest(noteDto);
        note.setValue(noteDto.getValue());

        Note updatedNote = noteRepository.save(note);
        return convertToDto(updatedNote);
    }

    @Override
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée"));
        noteRepository.delete(note);
    }

    @Override
    public List<NoteResponseDto> getNotesByEvaluation(Long evaluationId) {
        List<Note> notes = noteRepository.findByEvaluationId(evaluationId);
        return notes.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<NoteResponseDto> getNotesByStudent(Long studentId) {
        List<Note> notes = noteRepository.findByUserId(studentId);
        return notes.stream().map(this::convertToDto).toList();
    }

    private boolean isStudentInEvaluationClass(User student, Evaluation evaluation) {
        if (student.getClassroom() == null) return false;
        return evaluation.getClassrooms().contains(student.getClassroom());
    }

    private void validateNoteRequest(NoteRequestDto noteDto) {
        if (noteDto.getStudentId() == null) {
            throw new BadRequestException("L'ID de l'étudiant est obligatoire");
        }

        if (noteDto.getEvaluationId() == null) {
            throw new BadRequestException("L'ID de l'évaluation est obligatoire");
        }

        Evaluation evaluation = evaluationRepository.findById(noteDto.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        if (noteDto.getValue() < evaluation.getMinValue()) {
            throw new BadRequestException(
                    String.format("La note ne peut pas être inférieure à %d", evaluation.getMinValue())
            );
        }

        if (noteDto.getValue() > evaluation.getMaxValue()) {
            throw new BadRequestException(
                    String.format("La note ne peut pas dépasser %d", evaluation.getMaxValue())
            );
        }
    }

    private NoteResponseDto convertToDto(Note note) {
        return NoteResponseDto.builder()
                .id(note.getId())
                .value(note.getValue())
                .studentId(note.getUser().getId())
                .studentName(note.getUser().getFirstname() + " " + note.getUser().getLastname())
                .evaluationId(note.getEvaluation().getId())
                .evaluationTitle(note.getEvaluation().getTitle())
                .build();
    }
}
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;

    @Override
    public ApiResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée"));

        return ApiResponse.builder()
                .message("Note trouvée")
                .response(convertToDto(note))
                .build();
    }

    @Override
    public ApiResponse createNote(NoteRequestDto noteDto) {
        if (noteDto == null) {
            throw new BadRequestException("Les données de la note sont requises");
        }

        validateNoteRequest(noteDto);

        User student = userRepository.findById(noteDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        if (!student.isStudent()) {
            throw new BadRequestException("Seuls les étudiants peuvent recevoir des notes");
        }

        Evaluation evaluation = evaluationRepository.findById(noteDto.getEvaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        if (!isStudentInEvaluationClass(student, evaluation)) {
            throw new BadRequestException("L'étudiant n'est pas concerné par cette évaluation");
        }

        Note note = new Note();
        note.setValue(noteDto.getValue());
        note.setUser(student);
        note.setEvaluation(evaluation);

        Note savedNote = noteRepository.save(note);
        return ApiResponse.builder()
                .message("Note enregistrée avec succès")
                .response(convertToDto(savedNote))
                .build();
    }

    @Override
    public ApiResponse updateNote(Long id, NoteRequestDto noteDto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée"));

        validateNoteRequest(noteDto);
        note.setValue(noteDto.getValue());

        Note updatedNote = noteRepository.save(note);
        return ApiResponse.builder()
                .message("Note mise à jour")
                .response(convertToDto(updatedNote))
                .build();
    }

    @Override
    public ApiResponse deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée"));

        noteRepository.delete(note);
        return ApiResponse.builder()
                .message("Note supprimée")
                .build();
    }

    @Override
    public ApiResponse getNotesByEvaluation(Long evaluationId) {
        List<Note> notes = noteRepository.findByEvaluationId(evaluationId);
        return ApiResponse.builder()
                .message(notes.size() + " note(s) trouvée(s)")
                .response(notes.stream().map(this::convertToDto).toList())
                .build();
    }

    @Override
    public ApiResponse getNotesByStudent(Long studentId) {
        List<Note> notes = noteRepository.findByUserId(studentId);
        return ApiResponse.builder()
                .message(notes.size() + " note(s) trouvée(s)")
                .response(notes.stream().map(this::convertToDto).toList())
                .build();
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
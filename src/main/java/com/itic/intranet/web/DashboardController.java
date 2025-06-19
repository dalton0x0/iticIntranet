package com.itic.intranet.web;

import com.itic.intranet.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v18/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final RoleService roleService;
    private final ClassroomService classroomService;
    private final EvaluationService evaluationService;
    private final NoteService noteService;

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getDashboardCount() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("users", userService.getAllUsers().size());
        counts.put("role", roleService.getAllRoles().size());
        counts.put("classrooms", classroomService.getAllClassrooms().size());
        counts.put("evaluations", evaluationService.getAllEvaluations().size());
        counts.put("notes", noteService.getAllNotes().size());
        return ResponseEntity.ok(counts);
    }
}

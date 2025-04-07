package com.itic.intranet;

import com.itic.intranet.dtos.*;
import com.itic.intranet.enums.RoleType;
import com.itic.intranet.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class IticIntranetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IticIntranetApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(
            RoleService roleService,
            ClassroomService classroomService,
            UserService userService,
            EvaluationService evaluationService,
            NoteService noteService
    ) {
        return args -> {
            RoleRequestDto admin = new RoleRequestDto();
            admin.setRoleType(RoleType.ADMIN);
            admin.setWording("Admin");
            roleService.createRole(admin);

            RoleRequestDto teacher = new RoleRequestDto();
            teacher.setRoleType(RoleType.TEACHER);
            teacher.setWording("Teacher");
            roleService.createRole(teacher);

            RoleRequestDto student = new RoleRequestDto();
            student.setRoleType(RoleType.STUDENT);
            student.setWording("Student");
            roleService.createRole(student);

            ClassroomRequestDto bachelorCda = new ClassroomRequestDto();
            bachelorCda.setName("Bachelor CDA");
            classroomService.createClassroom(bachelorCda);

            UserRequestDto adminUser = new UserRequestDto();
            adminUser.setFirstname("Admin");
            adminUser.setLastname("Admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setUsername("admin");
            adminUser.setPassword("admin1234");
            adminUser.setRoleType(RoleType.ADMIN);
            userService.createUser(adminUser);

            UserRequestDto teacherUser = new UserRequestDto();
            teacherUser.setFirstname("Teacher");
            teacherUser.setLastname("Bachelor");
            teacherUser.setEmail("teaxher@example.com");
            teacherUser.setUsername("teacher");
            teacherUser.setPassword("teacher1234");
            teacherUser.setRoleType(RoleType.TEACHER);
            userService.createUser(teacherUser);

            UserRequestDto studentUser = new UserRequestDto();
            studentUser.setFirstname("Student");
            studentUser.setLastname("Bachelor");
            studentUser.setEmail("studentUser@example.com");
            studentUser.setUsername("studentUser");
            studentUser.setPassword("student1234");
            studentUser.setRoleType(RoleType.STUDENT);
            userService.createUser(studentUser);

            userService.assignClassroomToUser(3L, 1L);
            //classroomService.addTeacherToClassroom(1L, 2L);
            //classroomService.addStudentToClassroom(1L, 3L);

            EvaluationRequestDto evaluation = new EvaluationRequestDto();
            evaluation.setTitle("Evaluation");
            evaluation.setDescription("This is the evaluation");
            evaluation.setMinValue(0);
            evaluation.setMaxValue(100);
            evaluation.setDate(LocalDateTime.now());
            evaluationService.createEvaluation(evaluation, 2L);

            evaluationService.addClassroomToEvaluation(1L, 1L);

            NoteRequestDto note = new NoteRequestDto();
            note.setValue(40);
            note.setStudentId(3L);
            note.setEvaluationId(1L);
            noteService.createNote(note);
        };
    }
}
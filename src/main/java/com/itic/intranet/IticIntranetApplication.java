package com.itic.intranet;

import com.itic.intranet.dtos.ClassroomRequestDto;
import com.itic.intranet.dtos.RoleRequestDto;
import com.itic.intranet.dtos.UserRequestDto;
import com.itic.intranet.models.*;
import com.itic.intranet.repositories.*;
import com.itic.intranet.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class IticIntranetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IticIntranetApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserService userService, RoleService roleService, ClassroomService classroomService, EvaluationService evaluationService, NoteService noteService) {
        return args -> {
            RoleRequestDto  roleRequestDto = new RoleRequestDto("Student");
            roleService.addRole(roleRequestDto);
            Role studentRole = roleService.getRoleByWording("Student");

            ClassroomRequestDto bachelorCda = new ClassroomRequestDto("Bachelor-CDA", null);
            classroomService.addClassroom(bachelorCda);

            UserRequestDto cheridanh = new UserRequestDto("Chéridanh", "TSIELA", "divintsiela@gmail.com", "CTSIELA", "P@ssw0rd", studentRole, null);

            List<User> listBachelorCdaStudents = new ArrayList<>();
            listBachelorCdaStudents.add(cheridanh);

            bachelorCda.setUsers(listBachelorCdaStudents);
            classroomRepository.save(bachelorCda);

            Evaluation makeMcd = new Evaluation(null, "Make MCD", "MCD for your Bachelor Project", 0, 500, LocalDateTime.now(), listBachelorCdaStudents, null);
            evaluationRepository.save(makeMcd);

            Note mcdNote1 = new Note(null, 125, cheridanh, makeMcd);
            noteRepository.save(mcdNote1);
            List<Note> listMcdNotes = new ArrayList<>();
            listMcdNotes.add(mcdNote1);makeMcd.setNotes(listMcdNotes);
            evaluationRepository.save(makeMcd);
        };
    }
}

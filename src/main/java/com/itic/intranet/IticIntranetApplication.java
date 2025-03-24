package com.itic.intranet;

import com.itic.intranet.models.*;
import com.itic.intranet.repositories.*;
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

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ClassroomRepository classroomRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    EvaluationRepository evaluationRepository;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Role student = new Role(null, "Student", null);
            roleRepository.save(student);

            Classroom bachelorCda = new Classroom(null, "Bachelor-CDA", null);
            classroomRepository.save(bachelorCda);

            User cheridanh = new User(null, "Chéridanh", "TSIELA", "divintsiela@gmail.com", "CTSIELA", "P@ssw0rd", student, null,  null, true);
            userRepository.save(cheridanh);

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

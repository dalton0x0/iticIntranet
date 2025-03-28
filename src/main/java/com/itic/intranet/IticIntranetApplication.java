package com.itic.intranet;

import com.itic.intranet.enums.RoleType;
import com.itic.intranet.models.*;
import com.itic.intranet.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class IticIntranetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IticIntranetApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            ClassroomRepository classroomRepository,
            EvaluationRepository evaluationRepository,
            NoteRepository noteRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Role admin = Role.builder()
                    .roleType(RoleType.ADMIN)
                    .wording("Administrateur")
                    .build();

            Role teacher = Role.builder()
                    .roleType(RoleType.TEACHER)
                    .wording("Prof")
                    .build();

            Role student = Role.builder()
                    .roleType(RoleType.STUDENT)
                    .wording("Étudiant")
                    .build();

            roleRepository.saveAll(List.of(admin, teacher, student));

            Classroom bachelorCda = Classroom.builder()
                    .name("Bachelor CDA")
                    .build();

            Classroom bachelorAis = Classroom.builder()
                    .name("Bachelor AIS")
                    .build();

            classroomRepository.saveAll(List.of(bachelorCda, bachelorAis));

            User admininistrateur = User.builder()
                    .firstname("Admin")
                    .lastname("ITIC")
                    .email("admin@itic.com")
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(admin)
                    .active(true)
                    .build();

            User malek = User.builder()
                    .firstname("Malek")
                    .lastname("CHAOUCH")
                    .email("mc@itic.com")
                    .username("miikelee")
                    .password(passwordEncoder.encode("prof123"))
                    .role(teacher)
                    .active(true)
                    .build();

            User adnan = User.builder()
                    .firstname("Adnan")
                    .lastname("RIHAN")
                    .email("ar@itic.com")
                    .username("axel")
                    .password(passwordEncoder.encode("prof123"))
                    .role(teacher)
                    .active(true)
                    .build();

            User cheridanh = User.builder()
                    .firstname("Cheridanh")
                    .lastname("TSIELA")
                    .email("ct@itic.com")
                    .username("dalton")
                    .password(passwordEncoder.encode("etu123"))
                    .role(student)
                    .classroom(bachelorCda)
                    .active(true)
                    .build();

            User loic = User.builder()
                    .firstname("Loic")
                    .lastname("TCHINDA")
                    .email("lt@itic.com")
                    .username("binguiste")
                    .password(passwordEncoder.encode("etu123"))
                    .role(student)
                    .classroom(bachelorCda)
                    .active(true)
                    .build();

            User louis = User.builder()
                    .firstname("Louis")
                    .lastname("Vidal")
                    .email("lv@itic.com")
                    .username("sioul")
                    .password(passwordEncoder.encode("etu123"))
                    .role(student)
                    .classroom(bachelorAis)
                    .active(true)
                    .build();

            userRepository.saveAll(List.of(admininistrateur, malek, adnan, cheridanh, loic, louis));

            adnan.getTaughtClassrooms().addAll(List.of(bachelorCda, bachelorAis));
            malek.getTaughtClassrooms().add(bachelorCda);
            userRepository.saveAll(List.of(adnan, malek));

            Evaluation conception = Evaluation.builder()
                    .title("Conception")
                    .description("Faire le diagrame de sequence de votre projet")
                    .minValue(0)
                    .maxValue(50)
                    .date(LocalDateTime.now().plusDays(7))
                    .createdBy(malek)
                    .build();

            Evaluation infra = Evaluation.builder()
                    .title("Haute disponibilité")
                    .description("Mettre en place une redondance AD et routeur Mikrotik")
                    .minValue(0)
                    .maxValue(50)
                    .date(LocalDateTime.now().plusDays(14))
                    .createdBy(adnan)
                    .build();

            Evaluation piscine = Evaluation.builder()
                    .title("Première Pisicine")
                    .description("Réalisez tous les challenges dans le temps impartis")
                    .minValue(0)
                    .maxValue(100)
                    .date(LocalDateTime.now().plusDays(14))
                    .createdBy(adnan)
                    .build();

            evaluationRepository.saveAll(List.of(conception, infra));

            conception.getClassrooms().add(bachelorCda);
            infra.getClassrooms().add(bachelorAis);
            piscine.getClassrooms().addAll(List.of(bachelorCda, bachelorAis));
            evaluationRepository.saveAll(List.of(conception, infra));

            Note noteConception1 = Note.builder()
                    .value(50)
                    .user(cheridanh)
                    .evaluation(conception)
                    .build();

            Note noteConception2 = Note.builder()
                    .value(40)
                    .user(loic)
                    .evaluation(conception)
                    .build();

            Note noteInfra = Note.builder()
                    .value(18)
                    .user(louis)
                    .evaluation(infra)
                    .build();

            noteRepository.saveAll(List.of(noteConception1, noteConception2, noteInfra));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

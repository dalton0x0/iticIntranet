package com.itic.intranet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "classrooms")  // mappedBy fait référence à Evaluation.classrooms
    @JsonIgnore
    private List<Evaluation> evaluations = new ArrayList<>();

    @ManyToMany(mappedBy = "taughtClassrooms")
    @JsonIgnore
    private List<User> teachers = new ArrayList<>();
}
package com.eduManage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "instructors")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // link to your User entity

    private String bio;
    private String expertise;
    private String profilePicture;

    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;
}

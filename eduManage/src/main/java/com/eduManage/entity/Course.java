package com.eduManage.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private double price;
    private String duration;
    private String lessons;
    private String language;
    private String certifications;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}

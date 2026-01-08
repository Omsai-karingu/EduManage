package com.eduManage.bean;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    private String title;
    private double price;
    private String duration;
    private String lessons;
    private String language;
    private String certifications;
    private Integer categoryId;
    private Integer instructorId; // required for ADMIN
    private String imagepath;
}

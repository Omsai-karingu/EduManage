package com.eduManage.bean;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponse {
	private String status;
    private int code;
    private Object data;
    private String message;
}

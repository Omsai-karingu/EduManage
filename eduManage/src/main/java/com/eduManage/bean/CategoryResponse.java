package com.eduManage.bean;
import com.eduManage.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
private String status;
private int code;
private Object data;
private String message;

public CategoryResponse(String status, int code, Category data, String message) {
    this.status = status;
    this.code = code;
    this.data = data;
    this.message = message;
}
}

package com.eduManage.bean;

import com.eduManage.entity.User.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
	private String token;
	private String email;
	private Role role;
	
	public AuthResponse(String token, String email, Role role2) {
        this.token = token;
        this.email = email;
        this.role = role2;
    }
}

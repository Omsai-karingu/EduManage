package com.eduManage.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_details")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@Column(unique = true)
	private String email;

	private String password;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	private String otp;
	
	private LocalDateTime otpExpiry;
	public enum Role {
	STUDENT,INSTRUCTOR,ADMIN
	}
}

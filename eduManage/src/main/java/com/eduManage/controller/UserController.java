package com.eduManage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduManage.entity.User;
import com.eduManage.entity.User.Role;
import com.eduManage.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public User updateUserRole(
            @PathVariable int id,
            @RequestParam Role role) {

        User user = userService.getUserById(id);
        user.setRole(role);
        return userService.updateUser(id, user);
    }
	
}

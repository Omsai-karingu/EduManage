package com.eduManage.serviceimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.eduManage.service.EmailService;

@Service
public class EmailServiceimpl implements EmailService{
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Override
	public void sendOtp(String to, String otp) {
		// TODO Auto-generated method stub
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject("Password Reset Otp");
		msg.setText("Tour Otp is" + otp);
		
		mailSender.send(msg);
	}

}

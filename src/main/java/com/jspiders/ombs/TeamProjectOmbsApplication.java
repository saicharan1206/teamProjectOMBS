package com.jspiders.ombs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class TeamProjectOmbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamProjectOmbsApplication.class, args);
	}
	
//	@Bean
//	public JavaMailSender getMailSender() {
//		return new JavaMailSenderImpl();
//	}

}

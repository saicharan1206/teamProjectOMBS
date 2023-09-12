package com.jspiders.ombs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
@EnableJpaAuditing(auditorAwareRef = "auditorResolver")
public class TeamProjectOmbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamProjectOmbsApplication.class, args);
		System.out.println("Helloo");
	}

}

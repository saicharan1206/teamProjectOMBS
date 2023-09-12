package com.jspiders.ombs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.jspiders.ombs.util.SpringsSecurityAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TeamProjectOmbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamProjectOmbsApplication.class, args);
	}
	
	 @Bean
     public AuditorAware<String> auditorAware()
	{
		return new SpringsSecurityAuditing();
	}

}

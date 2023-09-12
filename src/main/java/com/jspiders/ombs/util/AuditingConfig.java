package com.jspiders.ombs.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfig 
{
	@Bean
	public AuditingImple auditorAware(){
		return new AuditingImple();
	}
}

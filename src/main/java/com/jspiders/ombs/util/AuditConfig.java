package com.jspiders.ombs.util;


import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "audit")
public class AuditConfig {
	@Bean
	public AuditorAware<String> audit(){
		return ()->Optional.of("junaith");
	}
	

}

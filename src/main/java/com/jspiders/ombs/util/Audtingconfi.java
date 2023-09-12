package com.jspiders.ombs.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/***
 * 
 * it return the bean object to the ioc container.
 *
 */
@Configuration
@EnableJpaAuditing
public class Audtingconfi {

	@Bean
	public Auditingimpl auditorAware(){
		return new Auditingimpl();
	}
}

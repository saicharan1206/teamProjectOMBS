package com.jspiders.ombs.util;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditingImple implements AuditorAware<String>{

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("Bhuvana");
	}

}

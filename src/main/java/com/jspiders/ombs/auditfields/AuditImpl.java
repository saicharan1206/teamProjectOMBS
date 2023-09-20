package com.jspiders.ombs.auditfields;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		// TODO Auto-generated method stub
		return Optional.of("Shaila");
	}

	
}

package com.jspiders.ombs.util;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/*** 
 	implements the funcational interface AuditorAware and it has getcurrentAuditor method
 	return the current user name.
 ***/
public class Auditingimpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("yogesh");
	}

}

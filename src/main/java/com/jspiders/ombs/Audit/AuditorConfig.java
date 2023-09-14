package com.jspiders.ombs.Audit;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditorConfig implements AuditorAware<String>{

	@Override
	public Optional<String> getCurrentAuditor() {
		
		return Optional.of("Ganesh");
	}

}

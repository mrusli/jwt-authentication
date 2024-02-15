package com.bezkoder.springjwt.auditing;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;

public class ApplicationAuditAware implements AuditorAware<Long> {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationAuditAware.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder
				.getContext()
				.getAuthentication();
		if (authentication == null ||
			!authentication.isAuthenticated() ||
			authentication instanceof AnonymousAuthenticationToken) {
			
			return Optional.empty();
		}
		
		logger.info("Authentication Details: "+authentication.getDetails());
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		logger.info("userDetailsImpl id: "+userDetailsImpl.getId());
		Optional<User> user = userRepository.findById(userDetailsImpl.getId());
		logger.info("Username: "+ user.get().getUsername());
		
		// User userPrincipal = (User) authentication.getPrincipal();
		return Optional.ofNullable(user.get().getId());
	}

}

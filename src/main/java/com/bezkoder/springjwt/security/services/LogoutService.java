package com.bezkoder.springjwt.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService implements LogoutHandler {

	private final TokenRepository tokenRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(LogoutService.class);
	
	public LogoutService(TokenRepository tokenRepository) {
		super();
		this.tokenRepository = tokenRepository;
		
		logger.info("Logout Service...");
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		logger.info("Loggin out...");
		
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		
		jwt = authHeader.substring(7);
		var storedToken = getTokenRepository().findByToken(jwt)
				.orElse(null);
		logger.info(storedToken==null ? "null" : storedToken.toString());
		
		if (storedToken != null) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			getTokenRepository().save(storedToken);
			
			SecurityContextHolder.clearContext();
		}
	}

	public TokenRepository getTokenRepository() {
		return tokenRepository;
	}

}

package com.bezkoder.springjwt.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.bezkoder.springjwt.security.jwt.AuthEntryPointJwt;
import com.bezkoder.springjwt.security.jwt.AuthTokenFilter;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@SuppressWarnings("unused")
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	private final LogoutHandler logoutHandler;
	
	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
    public WebSecurityConfig(LogoutHandler logoutHandler) {
		super();
		this.logoutHandler = logoutHandler;
		logger.info("Configuration - WebSecurity - SecurityConfiguration Started");
    }

	@Bean
    AuthTokenFilter authenticationJwtTokenFilter() {
		logger.info("AuthTokenFilter - Create a new AuthTokenFilter");
		
		return new AuthTokenFilter();
	}
	
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
    	logger.info("DaoAuthenticationProvider - Create a new DaoAuthenticationProvider");
    	
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
    	authProvider.setUserDetailsService(userDetailsService);
    	authProvider.setPasswordEncoder(passwordEncoder());
   
    	return authProvider;
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    	logger.info("AuthenticationManager...");
    	
		return authConfig.getAuthenticationManager();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
    	logger.info("Create a new BCryptPasswordEncoder");
    	
		return new BCryptPasswordEncoder();
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	logger.info("Define Security FilterChain");
    	
    	http.csrf(csrf -> csrf.disable())
	        // .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
	        .sessionManagement(session -> 
	        	session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> 
	        	auth.requestMatchers("/api/auth/**").permitAll()
	              .requestMatchers("/api/test/**").permitAll()
	              .anyRequest().authenticated())
	        .logout(logout -> 
	        	logout.logoutUrl("/api/test/logout")
	        		.addLogoutHandler(logoutHandler)
	        		.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
    
    	http.authenticationProvider(authenticationProvider());

    	http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    	return http.build();
	}

	public LogoutHandler getLogoutHandler() {
		return logoutHandler;
	}

}

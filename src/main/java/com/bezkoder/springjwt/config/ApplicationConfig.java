package com.bezkoder.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import com.bezkoder.springjwt.auditing.ApplicationAuditAware;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    AuditorAware<Long> auditorAware() {
    	return new ApplicationAuditAware();
	}
}

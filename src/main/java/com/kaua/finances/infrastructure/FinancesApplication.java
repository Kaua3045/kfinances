package com.kaua.finances.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class FinancesApplication {

	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
		SpringApplication.run(FinancesApplication.class, args);
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.csrf().disable().build();
	}
}

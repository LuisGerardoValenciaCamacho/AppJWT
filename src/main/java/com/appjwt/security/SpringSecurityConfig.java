package com.appjwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {
	
	private AuthenticationConfiguration authenticationConfiguration;
	
	public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
		this.authenticationConfiguration = authenticationConfiguration;
	}
	
	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(authz -> authz
			.requestMatchers(HttpMethod.GET, "/api/users").permitAll()
			.requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
			.requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/products/findAll", "/api/products/find/{id}").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.POST, "/api/products/add").hasRole("ADMIN")
			.requestMatchers(HttpMethod.PUT, "/api/products/update/{id}").hasRole("ADMIN")
			.requestMatchers(HttpMethod.DELETE, "/api/products/delete/{id}").hasRole("ADMIN")
			.anyRequest()
			.authenticated()
		)
		.addFilter(new JwtAuthenticationFilter(authenticationManager()))
		.addFilter(new JwtValidationfilter(authenticationManager()))
		.csrf(config -> config.disable())
		.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.build();
	}
}

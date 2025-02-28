package com.appjwt.security;

import static com.appjwt.security.TokenJwtConfig.CONTENT_TYPE;
import static com.appjwt.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.appjwt.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.appjwt.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appjwt.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		User user = null;
		try {
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		org.springframework.security.core.userdetails.User user =  (org.springframework.security.core.userdetails.User)authResult.getPrincipal();
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		Claims claims = Jwts
			.claims()
			.add("authorities", new ObjectMapper().writeValueAsString(roles))
			.add("username", user.getUsername())
			.build();
		
		String token = Jwts.builder()
			.subject(user.getUsername())
			.claims(claims)
			.expiration(new Date(System.currentTimeMillis() + 3600000))
			.issuedAt(new Date())
			.signWith(SECRET_KEY)
			.compact();
		
		response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
		
		Map<String, String> body = new HashMap<>();
		body.put("token", token);
		body.put("username", user.getUsername());
		body.put("message", String.format("Hola %s has iniciado sesión con exito!",  user.getUsername()));
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(200);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);
		Map<String, String> body = new HashMap<>();
		body.put("message", "Error en la autenticación username o password incorrectos");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(200);
	}
}

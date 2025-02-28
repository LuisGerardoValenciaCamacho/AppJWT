package com.appjwt.validation;

import org.springframework.stereotype.Component;

import com.appjwt.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistByUsernameValidation implements ConstraintValidator<ExistByUsername, String> {
	
	private UserService service;
	
	public ExistByUsernameValidation(UserService userService) {
		this.service = userService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !service.existByUsername(value);
	}

}

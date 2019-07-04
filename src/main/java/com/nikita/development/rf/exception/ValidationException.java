package com.nikita.development.rf.exception;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.nikita.development.rf.dto.RegisterDTO;

public class ValidationException extends RuntimeException {
	
	RegisterDTO register;
	Errors errors;
	
	public ValidationException() {
		
	}
	
	public ValidationException(Errors list, RegisterDTO r){
		this.errors = list;
		this.register = r;
	}
	
	public Errors getErrors() {
		return errors;
	}
	
	public RegisterDTO getRegister() {
		return register;
	}
	
	public void setRegister(RegisterDTO r) {
		register = r;
	}
}

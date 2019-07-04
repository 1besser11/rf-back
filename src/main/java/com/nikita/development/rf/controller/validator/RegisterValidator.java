package com.nikita.development.rf.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.nikita.development.rf.dto.RegisterDTO;


@Component("beforeCreateRegisterValidator")
public class RegisterValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return RegisterDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
        RegisterDTO p = (RegisterDTO) target;
        
        if (p.getLogin() == null || p.getLogin().length() == 0) {
        	errors.rejectValue("login", "empty");
        }
        else if (p.getLogin().matches("^[A-Za-z]{2,40}$")) {
        	errors.rejectValue("login", "not_correct_format");
        }
        if (p.getFirstName() == null || p.getFirstName().length() == 0) {
        	errors.rejectValue("firstName", "empty");
        }
        else if (!p.getFirstName().matches("^([A-Z\\sa-z-]{2,40}|[А-Щ\\sЬЮЯІЇЄа-щьюяіїєґ-]{2,40})$")) {
        	errors.rejectValue("firstName", "not_correct_format");
        }
        if (p.getSurname() == null || p.getSurname().length() == 0) {
        	errors.rejectValue("surname", "empty");
        }
        else if (!p.getSurname().matches("^([A-Z\\sa-z-]{2,40}|[А-Щ\\sЬЮЯІЇЄа-щьюяіїєґ-]{2,40})$")) {
        	errors.rejectValue("surname", "not_correct_format");
        }
        
        
        
	}

}

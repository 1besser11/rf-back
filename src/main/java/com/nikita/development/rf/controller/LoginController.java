package com.nikita.development.rf.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.nikita.development.rf.controller.validator.RegisterValidator;
import com.nikita.development.rf.dto.RegisterDTO;
import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.exception.AlreadyExistsException;
import com.nikita.development.rf.exception.InvalidTokenException;
import com.nikita.development.rf.exception.ValidationException;
import com.nikita.development.rf.security.model.RefreshToken;
import com.nikita.development.rf.security.model.TokenResponse;
import com.nikita.development.rf.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @Autowired
    private UserService userService;
    
    
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = "application/json")
    public TokenResponse login(@RequestBody User user) {
        System.out.println("login " + LocaleContextHolder.getLocale());
        TokenResponse token = userService.login(user);
        System.out.println(token);
        return token;
    }
    
    @RequestMapping(value = "/auth/token", method = RequestMethod.POST, produces = "application/json")
    public TokenResponse refreshToken(@RequestBody RefreshToken tokenIn) {
        System.out.println("refresh ");
        
        TokenResponse token = userService.refresh(tokenIn);
        
        return token;
    }
	
    
    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<?> handleExpired(ExpiredJwtException e) {
    	return ResponseEntity.status(401).body("{\"error\": \"" + e.getMessage() + "\"}");
    }
    
    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<?> handleException(AlreadyExistsException e) {
    	return ResponseEntity.status(409).body("{\"error\": \"" + e.getMessage() + "\"}");
    }
    
    
    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<?> handleException(InvalidTokenException e) {
    	return ResponseEntity.status(400).body("{\"error\": \"" + e.getMessage() + "\"}");
    }
    
    
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handleException(ValidationException e) {
    	/*ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    	String msg = "";
    	for(ObjectError o: e.getErrors().getAllErrors()) {
    		try {
    			msg += messageSource.getMessage(o, Locale.getDefault()) + "\n";
    		}
    		catch(Exception exception){
    			msg += o.getCode() + "\n";
    		}
    	}
    	System.out.println(e.getErrors().getAllErrors());
    	*/
    	
    	String msg = "";
    	for(ObjectError o: e.getErrors().getAllErrors()) {
    		msg += o; 
    	}
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + msg + "\"}");
    }


}

package com.nikita.development.rf.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.nikita.development.rf.controller.model.ErrorMessage;
import com.nikita.development.rf.controller.validator.RegisterValidator;
import com.nikita.development.rf.dto.RegisterDTO;
import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.exception.AlreadyExistsException;
import com.nikita.development.rf.exception.ValidationException;
import com.nikita.development.rf.security.filter.JwtTokenUtil;
import com.nikita.development.rf.security.model.RefreshToken;
import com.nikita.development.rf.security.model.TokenResponse;
import com.nikita.development.rf.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RegisterController {

    @Autowired
    private UserService userService;
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new RegisterValidator());
    }
    

    
    @RequestMapping(value = "/auth/register", method = RequestMethod.POST, produces = "application/json")
    public String register(@Valid @RequestBody RegisterDTO user, Errors errors) {
    	
    	if(errors.hasErrors()) {
    		throw new ValidationException(errors, user);
    	}
        userService.register(user);
        return "";
    }
    
    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<?> handleExpired(ExpiredJwtException e) {
    	return ResponseEntity.status(401).body("{\"error\": \"" + e.getMessage() + "\"}");
    }
    
    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<?> handleException(AlreadyExistsException e) {
    	return ResponseEntity.status(409).body("{\"error\": \"" + e.getMessage() + "\"}");
    }
    
    @ExceptionHandler({ValidationException.class})
    public @ResponseBody ErrorMessage<RegisterDTO> handleException(ValidationException e) throws JsonGenerationException, JsonMappingException, IOException {
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
    	RegisterDTO user = e.getRegister();
    	String msg = "";
    	for(ObjectError o: e.getErrors().getAllErrors()) {
    		FieldError er = null;
    		try {
    			er = (FieldError) o;
    			msg += er.getField() + " : " + o.getCode() + ". "; 
				user.getClass().getDeclaredField(er.getField()).set(user, "");
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		
    	}
    	return new ErrorMessage<RegisterDTO>(user, msg);
     }


}
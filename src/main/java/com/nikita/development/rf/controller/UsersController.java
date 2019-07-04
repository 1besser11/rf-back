package com.nikita.development.rf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class UsersController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/api/users")
	@PreAuthorize("hasAuthority('READ_USERS')")
	public List<User> getEvents(@RequestParam(required = false, defaultValue = "10") int limit,
	                                 @RequestParam(required = false, defaultValue = "0") int page) {
		List<User> us =  userService.findAll(limit, page);
	    for (User u : us)
	    	System.out.println(u);
	    return us;
	}
	
	@DeleteMapping("/api/users/{id}")
	@PreAuthorize("hasAuthority('DELETE_USERS')")
	public String deleteEvent(@PathVariable("id") long id) {
		userService.delete(id);
		return "smth";
	    
	}


}

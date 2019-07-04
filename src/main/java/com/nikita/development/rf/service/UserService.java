package com.nikita.development.rf.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nikita.development.rf.dto.RegisterDTO;
import com.nikita.development.rf.entity.*;
import com.nikita.development.rf.exception.AlreadyExistsException;
import com.nikita.development.rf.exception.InvalidTokenException;
import com.nikita.development.rf.repository.RefreshTokenRepository;
import com.nikita.development.rf.repository.UserRepository;
import com.nikita.development.rf.security.filter.JwtTokenUtil;
import com.nikita.development.rf.security.model.*;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Service
public class UserService implements IService<User> {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
    @Autowired
    private UserRepository repository;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

	@Override
	public List<User> findAll(int size, int page) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	public User save(User entity) {
		//TODO: make transactional
		if(find(entity.getLogin())!= null) {
			throw new AlreadyExistsException("User with given login already exists");
		}
		return repository.save(entity);
		
	}

	@Override
	public User find(long id) {
		return repository.findById(id);
	}

	@Override
	public User update(long id, User entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long id) {
		repository.delete(find(id));
		
	}


	public User find(String name) {
		return repository.findByLogin(name);
		
	}

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;
 
    
    public String signUp(User user) {
        User dbUser = userRepository.findByLogin(user.getLogin());
        if (dbUser != null) {
            throw new RuntimeException("User already exist.");
        }
        User userToCreate = new User();
        userToCreate.setLogin(user.getLogin());
        
        userToCreate.setPassword(encode(user.getPassword()));
        
        save(userToCreate);
        return jwtTokenUtil.generateToken(userToCreate);
    }
    
    
    public TokenResponse login(User user) {
        User dbUser = userRepository.findByLogin(user.getLogin());
        if (dbUser == null) {
            throw new RuntimeException("No user with given login exists");
        }
        
        if(!encoder.matches(user.getPassword(), dbUser.getPassword())) {
        	throw new RuntimeException("No user with given login and pass exists");
        }
        
        TokenResponse token = new TokenResponse(jwtTokenUtil.generateToken(dbUser), 
        		jwtTokenUtil.generateRefresh(dbUser));
        
        RefreshToken refresh = new RefreshToken();
        
        refresh.setToken(token.getRefreshToken());
        refresh.setUser(dbUser);
        refreshTokenRepository.save(refresh);
        dbUser.getRefreshToken().add(refresh);
        repository.save(dbUser);
        
        return token;
        
       
    }
    
    public void register(RegisterDTO user){
    	if(!user.getConfirmPassword().equals(user.getPassword())) {
    		throw new RuntimeException("Password is incorrect");
    	}
    	
    	System.out.println(user);
    	User userdb = new User();
    	userdb.setEmail(user.getEmail());
    	userdb.setFirstName(user.getFirstName());
    	userdb.setLogin(user.getLogin());
    	userdb.setPassword(encode(user.getPassword()));
    	userdb.setPriveleges(new ArrayList<String>(Arrays.asList("READ_USERS")));// , "DELETE_USERS"
    	userdb.setSurname(user.getSurname());
    	save(userdb);
    	
    	
    }
    
    public User findByRefreshToken(RefreshToken tokenIn) {
    	long id = 0;
		String token = tokenIn.getToken();
		
        if (token != null) {
            try {
                id = jwtTokenUtil.getIdFromToken(token);
                
            } catch (IllegalArgumentException e) {
                System.out.println("an error occured during getting username from token" + e);
            } catch (ExpiredJwtException e) {
            	System.out.println("the token is expired and not valid anymore" + e);
            	
            } catch(SignatureException e){
            	System.out.println("Authentication Failed. Username or Password not valid.");
            }
        } else {
        	System.out.println("couldn't find bearer string, will ignore the header");
        }
        User user = repository.findById(id);
        
        List<RefreshToken> list = user.getRefreshToken().stream()
        		.filter(x->x.getToken().equals(token))
        		.collect(Collectors.toCollection(ArrayList::new)); 
        if(list.size() == 0) {
        	throw new InvalidTokenException("Given refresh not exists");
        }
        
        
        user.getRefreshToken().remove(list.get(0));
        refreshTokenRepository.deleteById(list.get(0).getId());
        repository.save(user);
        return user;
    }

	public TokenResponse refresh(RefreshToken tokenIn) {
        User user = findByRefreshToken(tokenIn);
        if(user==null) {
        	throw new RuntimeException("User not found");
        }
        
        String token = tokenIn.getToken();
        if(!jwtTokenUtil.validateToken(token, user))
        	throw new RuntimeException("Token is invalid");
        
        TokenResponse tokenResponse = new TokenResponse(jwtTokenUtil.generateToken(user), 
        		jwtTokenUtil.generateRefresh(user));
        
        RefreshToken refresh = new RefreshToken();
        refresh.setToken(tokenResponse.getRefreshToken());
        refresh.setUser(user);
        refreshTokenRepository.save(refresh);
        user.getRefreshToken().add(refresh);
        repository.save(user);
        
        return tokenResponse;
	}
	
	private String encode(String s) {
		return encoder.encode(s);
	}
}
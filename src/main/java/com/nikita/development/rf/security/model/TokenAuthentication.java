package com.nikita.development.rf.security.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nikita.development.rf.entity.User;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TokenAuthentication implements Authentication {
	
    private String token;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAuthenticated;
    private Object details;
    private User principal;



    public TokenAuthentication(String token, Collection<? extends GrantedAuthority> collection, boolean isAuthenticated, 
                                                User principal) {
        this.token = token;
        this.authorities = collection;
        this.isAuthenticated = isAuthenticated;
        this.principal = principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
    

    public void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public String getName() {
        if (principal != null)
            return ((UserDetails) principal).getUsername();
        else
            return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        isAuthenticated = b;
    }

    public String getToken() {
        return token;
    }

}

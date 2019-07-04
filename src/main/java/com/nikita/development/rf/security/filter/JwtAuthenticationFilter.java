package com.nikita.development.rf.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.exception.InvalidTokenException;
import com.nikita.development.rf.repository.UserRepository;
import com.nikita.development.rf.security.model.TokenAuthentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenUtil.resolveToken(req);
        if(token == null || token.equals("")) {
        	chain.doFilter(req, res);
        	return;
        }
        
        long id = 0;
        if (token != null) {
            try {
                id = jwtTokenUtil.getIdFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
                throw new RuntimeException();
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
                res.setStatus(401);
                res.getWriter().write("{\"status\": \"expired\"}");
                return;
            } catch(SignatureException e){
                logger.error("Authentication Failed. Username or Password not valid.");
                throw new RuntimeException();
            }
        } 
        
        if (id != 0 && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = repository.findById(id);
            
            if (user != null && jwtTokenUtil.validateToken(token, user)) {
                TokenAuthentication authentication = new TokenAuthentication(token ,user.getAuthorities(), true, user);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            }
        }
        
        chain.doFilter(req, res);
        
    }
}

package com.nikita.development.rf.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nikita.development.rf.entity.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;



@Component
public class JwtTokenUtil implements Serializable {
	
	

    public long getIdFromToken(String token) {
        return Long.parseLong(getClaimFromToken(token, Claims::getSubject));
    }

    public String getRefreshIdFromToken(String token) {
    	 final Claims claims = getAllClaimsFromToken(token);
    	 return (String) claims.get("refreshId");
    }
    
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Constants.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    public String generateRefresh(User user) {
    	
    	Claims claims = Jwts.claims().setSubject(Long.toString(user.getId()));//TODO: getId
      
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("https://devglan.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.REFRESH_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY)
                .compact();
        
    }

    public String generateToken(User user) {
        return doGenerateToken(user);
    }

    private String doGenerateToken(User user) {

        Claims claims = Jwts.claims().setSubject(Long.toString(user.getId()));//TODO: getId
        claims.put("scopes", user.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList()));
       // claims.put("refreshId", user.getRefreshId());
        claims.put("name", user.getLogin());
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("SomeIssuer")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, User userDetails) {
        final long id = getIdFromToken(token);
        return id == userDetails.getId() && !isTokenExpired(token);
    }
    
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
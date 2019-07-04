package com.nikita.development.rf.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("token");
    	if (response.isCommitted()) {
            return;
        }
    	DefaultOAuth2User oidcUser = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oidcUser.getAttributes();
        System.out.println(attributes);
        String email = (String) attributes.get("email");
        User user = userRepository.findByLogin(email);
        if (user == null) {
        	user = new User();
        	user.setLogin(email);
        	user.setPassword("def");
        	userRepository.save(user);
        }
        String token = jwtTokenUtil.generateToken(user);
        String redirectionUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/home")
                .queryParam("auth_token", token)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
    }

}

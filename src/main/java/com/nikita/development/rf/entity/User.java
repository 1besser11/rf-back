package com.nikita.development.rf.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nikita.development.rf.security.model.RefreshToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@EqualsAndHashCode(exclude = {"refreshToken"})
@ToString(exclude = {"refreshToken"})
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private long id;
   
    private String userIdGoogle;
    private double total;
    
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "user_discount",
            joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "discount", referencedColumnName = "id"))
    private Set<PercentDiscount> discounts;

  //@NotBlank(message = "Blank login")
  //@Size(min = 2, max = 45, message = "Wrong login size")
    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    private String email;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<RefreshToken> refreshToken;

//    @NotBlank(message = "Blank first name")
//    @Size(min = 2, max = 45, message = "Wrong first name size")
//    @Pattern(regexp = "[A-Z]", message = "Wrong syntax in first name")
    @Column(name = "first_name")
    private String firstName;
    //
//    @NotBlank(message = "Blank second name")
//    @Size(min = 2, max = 45, message = "Wrong second name size")
//    @Pattern(regexp = "[A-Z]", message = "Wrong syntax in second name")
//    @Column(name = "second_name")
    private String surname;

    public User() {

    }

    public User(Map<String, String> userInfo, OAuth2AccessToken token) {
        this.userIdGoogle = userInfo.get("sub");
        this.login = userInfo.get("email");
        this.password = userIdGoogle;
        //this.token = token;
    }


    public User(Map<String, String> authInfo) {
        this.userIdGoogle = authInfo.get("sub");
        this.login = authInfo.get("email");
    }
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> priveleges = new ArrayList<>();
    
    public List<String> getPriveleges() {
    	return priveleges;
    }
    
    public void setPriveleges(List<String> priveleges) {
    	this.priveleges = priveleges;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.priveleges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    

}

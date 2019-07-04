package com.nikita.development.rf.dto;

import javax.persistence.Column;

import com.nikita.development.rf.entity.User;

import lombok.*;

@Data
public class RegisterDTO {

	public String confirmPassword;
    public String login;
    public String password;
    public String email;
    public String firstName;
    public String surname;

}

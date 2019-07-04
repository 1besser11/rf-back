package com.nikita.development.rf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.nikita.development.rf.entity.User;

@CrossOrigin(origins = "http://localhost:4200")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);
    
    User findById(long id);
    
    User findByLoginAndPassword(String login, String password);
}
package com.nikita.development.rf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.nikita.development.rf.entity.Order;

@CrossOrigin(origins = "http://localhost:4200")
public interface OrderRepository extends JpaRepository<Order, Long> {

}

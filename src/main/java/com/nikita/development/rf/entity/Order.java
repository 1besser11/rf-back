package com.nikita.development.rf.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Order {
	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private long id;
	
	@OneToMany
	private User client;
	
	@OneToMany
	private Taxi taxi;
	
	@Enumerated(EnumType.STRING)
	private City from;
	@Enumerated(EnumType.STRING)
	private City to;
	
	private double price;
	private double distance;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;
	
	@Column(name = "finished_at", columnDefinition = "TIMESTAMP")
	private LocalDateTime finishedAt;

}

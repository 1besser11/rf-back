package com.nikita.development.rf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Taxi {
	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private long id;
	
	private String name;
	private String surname;
	
	private String carName;
	private String number;
	
	@Enumerated(EnumType.STRING)
	private TypeOfTaxi type;
	
	@Enumerated(EnumType.STRING)
	private City currentLocation;
	
	@Enumerated(EnumType.STRING)
	private TaxiStatus status;
}

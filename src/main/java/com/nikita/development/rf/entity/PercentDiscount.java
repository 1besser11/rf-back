package com.nikita.development.rf.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import lombok.Data;

@Entity
@Data
@Inheritance(
	    strategy = InheritanceType.JOINED
	)
public abstract class PercentDiscount {
	
	private double percent;
	private String name;
	
	@ManyToMany(mappedBy = "discounts")
	private Set<User> users;
	
	public abstract double calculateDiscountForOrder(Order o);

}

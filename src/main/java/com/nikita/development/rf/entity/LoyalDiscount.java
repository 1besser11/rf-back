package com.nikita.development.rf.entity;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class LoyalDiscount extends PercentDiscount {
	
	private boolean enabled = true;
	
	@Override
	public double calculateDiscountForOrder(Order o) {
	
		if(enabled == true)
		{
			if (o.getClient().getTotal() < 5000)
				return 0;
			else if(o.getClient().getTotal() < 10000)
				return o.getPrice()*0.05;
			else
				return o.getPrice()*0.1;
		}
		return 0;
	}

}

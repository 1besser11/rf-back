package com.nikita.development.rf.service;

import org.springframework.stereotype.Service;

import com.nikita.development.rf.entity.Order;
import com.nikita.development.rf.exception.NotEnoughMoneyAvailableException;

@Service
public class BalanceService {

	public void payForOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

	public void freezeMoneyForOrder(Order order) throws NotEnoughMoneyAvailableException {
		// TODO Auto-generated method stub
		
	}

}

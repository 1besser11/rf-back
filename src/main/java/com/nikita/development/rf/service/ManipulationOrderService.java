package com.nikita.development.rf.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import com.nikita.development.rf.entity.City;
import com.nikita.development.rf.entity.Order;
import com.nikita.development.rf.entity.OrderStatus;
import com.nikita.development.rf.entity.Taxi;
import com.nikita.development.rf.entity.TaxiStatus;
import com.nikita.development.rf.entity.TypeOfTaxi;
import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.exception.NoAvaliableTaxiException;
import com.nikita.development.rf.exception.NotEnoughMoneyAvailableException;

@Service
public class ManipulationOrderService {

	@Autowired
	SecurityContext security;
	
	@Autowired
	TaxiService taxiService;
	
	@Autowired
	BalanceService balanceService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	LocationsService locationService;
	
	public Order createOrder(TypeOfTaxi type, City start, City end) throws NoAvaliableTaxiException {
		
		Taxi taxi = taxiService.findOneByTypeAndCurrentLocation(type, start);
		Order order = new Order();
		order.setPrice(calculatePrice(taxi));
		order.setDistance(locationService.distanceBetween(start, end));
		order.setClient((User) security.getAuthentication().getPrincipal());
		order.setTaxi(taxi);
		order.setCreatedAt(LocalDateTime.now());
		order.setStatus(OrderStatus.ORDERED);
		return orderService.save(order);
		
	}

	private double calculatePrice(Taxi taxi) {
		// TODO  realize
		return 1000;
	}
	
	public Order cancelOrder(Order order) {
		order = orderService.find(order.getId());
		order.setStatus(OrderStatus.CANCELLED);
		return orderService.save(order);
	}
	
	public Order confirmOrder(Order order) throws NoAvaliableTaxiException, NotEnoughMoneyAvailableException {
		order = orderService.find(order.getId());
		Taxi t = taxiService.find(order.getTaxi().getId());
		if(t.getStatus() == TaxiStatus.NOT_AVALIABLE) {
			order.setStatus(OrderStatus.CANCELLED);
			orderService.save(order);
			throw new NoAvaliableTaxiException();
		}
		balanceService.freezeMoneyForOrder(order);
		t.setStatus(TaxiStatus.NOT_AVALIABLE);
		return orderService.save(order);
	}
	
	public Order finishOrder(Order order) {
		order = orderService.find(order.getId());
		order.setStatus(OrderStatus.FINISHED);
		order.setFinishedAt(LocalDateTime.now());
		balanceService.payForOrder(order);
		return orderService.save(order);
	}
	
}

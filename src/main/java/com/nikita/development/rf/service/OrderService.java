package com.nikita.development.rf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.nikita.development.rf.entity.Order;
import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.repository.OrderRepository;

public class OrderService implements IService<Order> {
	
	@Autowired
	OrderRepository repository;

	@Override
	public List<Order> findAll(int size, int page) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	public Order save(Order entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order find(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order update(long id, Order entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		
	}

}

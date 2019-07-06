package com.nikita.development.rf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.nikita.development.rf.entity.City;
import com.nikita.development.rf.entity.Taxi;
import com.nikita.development.rf.entity.TaxiStatus;
import com.nikita.development.rf.entity.TypeOfTaxi;
import com.nikita.development.rf.entity.User;
import com.nikita.development.rf.exception.NoAvaliableTaxiException;
import com.nikita.development.rf.repository.TaxiRepository;
import com.nikita.development.rf.repository.UserRepository;

public class TaxiService implements IService<Taxi> {

    @Autowired
    private TaxiRepository repository;
	
	@Override
	public List<Taxi> findAll(int size, int page) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	public Taxi save(Taxi entity) {
		return repository.save(entity);
	}

	@Override
	public Taxi find(long id) {
		return repository.findById(id);
	}

	@Override
	public Taxi update(long id, Taxi entity) {
		entity.setId(id);
		return repository.save(entity);
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		
	}

	public Taxi findOneByTypeAndCurrentLocation(TypeOfTaxi type, City start) throws NoAvaliableTaxiException {
		List<Taxi> list = repository.findByTypeAndCurrentLocationAndStatus(type, start, TaxiStatus.AVALIABLE);
		if(list.isEmpty()) {
			throw new NoAvaliableTaxiException();
		}
		return list.get(0);
	}

}

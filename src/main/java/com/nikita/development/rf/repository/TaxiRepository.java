package com.nikita.development.rf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.nikita.development.rf.entity.City;
import com.nikita.development.rf.entity.Taxi;
import com.nikita.development.rf.entity.TaxiStatus;
import com.nikita.development.rf.entity.TypeOfTaxi;

@CrossOrigin(origins = "http://localhost:4200")
public interface TaxiRepository extends JpaRepository<Taxi, Long> {

    List<Taxi> findByTypeAndCurrentLocationAndStatus(TypeOfTaxi t, City c, TaxiStatus s);

    Taxi findById(long id);

}
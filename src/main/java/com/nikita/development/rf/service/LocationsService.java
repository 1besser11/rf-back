package com.nikita.development.rf.service;

import org.springframework.stereotype.Service;

import com.nikita.development.rf.entity.City;
import com.nikita.development.rf.entity.Location;

@Service
public class LocationsService {
	
	double[][] distances = {
			{10d, 20d, 30d, 40d, 50d},
			{10d, 20d, 30d, 40d, 50d},
			{10d, 20d, 30d, 40d, 50d},
			{10d, 20d, 30d, 40d, 50d},
			{10d, 20d, 30d, 40d, 50d},
	};

	public double distanceBetween(City start, City end) {
		return distanceBeteenLocations(getLocation(start), getLocation(end));
	}
	
	private Location getLocation(City c) {
		return new Location(c.getId());
	}
	
	private double distanceBeteenLocations(Location s, Location e) {
		return distances[s.getId()][e.getId()];
	}
}

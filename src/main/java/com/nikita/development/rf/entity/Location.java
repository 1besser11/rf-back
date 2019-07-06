package com.nikita.development.rf.entity;

import lombok.Data;

@Data
public class Location {
	private int id;
	
	private double lat;
	private double lon;
	
	public Location(int id){
		this.id = id;
	}

}

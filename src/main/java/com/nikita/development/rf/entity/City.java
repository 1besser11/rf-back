package com.nikita.development.rf.entity;

public enum City {
	KYIV(0),
	ODESSA(1),
	LVIV(2),
	KHARKIV(3),
	CHERNIVCI(4);
	
	private City(int id) {
		this.id = id;
	}
	
	private int id;
	public int getId() {
		return id;
	}

}

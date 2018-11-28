package com.example;

import com.hm.taxi.Car;

public class DriverStatusPost {
	
	private int id;
	private Car.Status state;
	private String[] newLocation;
	
	public DriverStatusPost(int id, Car.Status state, String houseNr, String street, String city, String country) {
		this.id = id;
		this.state = state;
		this.newLocation = new String[] {houseNr, street, city, country};
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the state
	 */
	public Car.Status getState() {
		return state;
	}

	/**
	 * @return the newLocation
	 */
	public String[] getNewLocation() {
		return newLocation;
	}

}

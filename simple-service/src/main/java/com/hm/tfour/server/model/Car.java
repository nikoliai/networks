package com.hm.tfour.server.model;
/**
 * Class representing cars
 * @author Olga Nikoliai
 *
 */
public class Car {

	private int id;
	private String[] startAddress;
	private String[] destinationAddress;
	private State state;
	private long destinationTime; 

/**
 * 
 * @param id id of the car (1,2 or 3)
 * @param startAddress: street, house nr, city and country 
 * @param targetAddress: street, house nr, city and country 
 * @param destinationTime desired arrival time in milliseconds
 */
	public Car(int id, String[] startAddress, String[] targetAddress, long destinationTime) {
		this.id = id;
		this.startAddress = startAddress;
		this.destinationAddress = targetAddress;
		this.state = State.INACTIVE;
		this.destinationTime = destinationTime;
	}
/**
 * States of a car
 * INACTIVE: car is inactive (driver has a brake)
 * FREE: car is active and free  
 * IN_TIME  car is occupied and will reach the destination in time
 * LATE: car is occupied and will be late
 *
 */
	public enum State {
		INACTIVE, FREE, IN_TIME, LATE, ON_ROAD
	}

	public int getID() {
		return this.id;
	}

	public long getDestinationTime() {
		return this.destinationTime;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setDestinationTime(long destinationTime) {
		this.destinationTime = destinationTime;
	}

	public void setDestinationAddress(String[] destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String[] getDestinationAddress() {
		return this.destinationAddress;
	}

	public void setStartAddress(String[] startAddress) {
		this.startAddress = startAddress;
	}

	public String[] getStartAddress() {
		return this.startAddress;
	}
	
}

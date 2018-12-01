package com.hm.tfour.server.model;

public class Car {

	private int id;

	private String[] startAddress;
	private String[] destinationAddress;
	private State state;
	private long destinationTime; //in millis

	public Car(int id) {
		this.id = id;
		this.state = State.INACTIVE;
		this.destinationTime = 0;
	}

	public Car(int id, String[] startAddress, String[] targetAddress, long destinationTime) {
		this.id = id;
		this.startAddress = startAddress;
		this.destinationAddress = targetAddress;
		this.state = State.INACTIVE;
		this.destinationTime = destinationTime;
	}

	public enum State {
		INACTIVE, FREE, IN_TIME, LATE
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

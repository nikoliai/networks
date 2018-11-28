package com.hm.simpleservice.server.model;


public class Car {
	
	private int id;
	
	private String startAddress;
	private String targetAddress;
	private String latitudeStart;
	private String longitudeStart;
	private String latitudeFinish;
	private String longigudeFinish;
	private State state;
	private int travelTime; //in millis
	private int destinationTime; //in millis
    
	public Car(int id) {
		this.id = id;
		this.state = State.INACTIVE;
		this.travelTime = 0;
		this.destinationTime = 0;
	}

    public Car(int id, String startAddress, String targetAddress) {
    	this.id = id;
    	this.startAddress = startAddress;
    	this.targetAddress = targetAddress;
    	this.state = State.IN_TIME;
    }
    
    public void updateTime() {
    	//TODO
    }
    
    public void setStartAddress(String startAddress) {
    	this.startAddress = startAddress;
    }
    
    public void setDestinationAddress(String destinationAddress) {
    	this.targetAddress = destinationAddress;
    }

    public void setLatitude(String latitude) {
        this.latitudeStart = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitudeStart = longitude;
    }

    public void setState(State status) {
        this.state = status;
        
    }
    
    public int getTravelTime() {
    	return this.travelTime;
    }

	public int getID() {
		return this.id;
	}
	
	public State getState() {
		return this.state;
	}
	
	public void setDestinationTime(int destinationTime) {
		this.destinationTime = destinationTime;
	}
	
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}
	
	public int getStartTime() {
		return destinationTime - travelTime;
	}

    public enum State {
        INACTIVE, FREE, IN_TIME, LATE
    };
}

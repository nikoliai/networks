package com.hm.taxi;

import java.io.Serializable;

public class Car {
    String latitudeStart;
    String longitudeStart;
    String latitudeFinish;
    String longigudeFinish;
    int travelTime;
    int destinationTime;
    Status status;
    String id;

    public Car(String id, String latitudeStart, String longitudeStart, String latitudeFinish, String longigudeFinish) {
        super();
        this.latitudeStart = latitudeStart;
        this.longitudeStart = longitudeStart;
        this.latitudeFinish = latitudeFinish;
        this.longigudeFinish = longigudeFinish;
        this.status = Status.InTime;
        this.id = id;
    }

    public enum Status implements Serializable {
        Inactive, Free, InTime, Late;

        public String getStatus() {
            return this.name();
        }
    };

    public void setLatitudeStart(String latitudeStart) {
        this.latitudeStart = latitudeStart;
    }

    public void setLongitudeStart(String longitudeStart) {
        this.longitudeStart = longitudeStart;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        switch (status.toLowerCase()) {
        case "inactive":
            setStatus(Status.Inactive);
            break;
        case "free":
            setStatus(Status.Free);
            break;
        case "intime":
            setStatus(Status.InTime);
            break;
        case "late":
            setStatus(Status.Late);
            break;
        default:
            setStatus(Status.Free);
        }
    }

    public String getLatitudeStart() {
        return latitudeStart;
    }

    public String getLongitudeStart() {
        return longitudeStart;
    }

    @Override
    public String toString() {
        return "id: " + id + " latstart: " + latitudeStart + " longstart: " + longitudeStart + " latfinish: " + latitudeFinish
                + " lonfin: " + longigudeFinish + " status: "+ this.status.getStatus();

    }
    
    

    /**
	 * @return the travelTime
	 */
	public int getTravelTime() {
		return travelTime;
	}

	/**
	 * @param travelTime the travelTime to set
	 */
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}

	/**
	 * @return the destinationTime
	 */
	public int getDestinationTime() {
		return destinationTime;
	}

	/**
	 * @param destinationTime the destinationTime to set
	 */
	public void setDestinationTime(int destinationTime) {
		this.destinationTime = destinationTime;
	}

	public String getId() {
        return id;
    }

	public String getLongitudeDestination() {
		return this.longigudeFinish;
	}

	public String getLatitudeDestination() {
		return this.latitudeFinish;
	}

}

package com.hm.tfour.server.ressource;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class DriverState implements Serializable{
	private String id;
	private String state; 
    private String location;
    
	public DriverState(String id, String state, String location) {
		this.id = id;
		this.state = state;
		this.location = location;
	}

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public DriverState()
    {
    }
}

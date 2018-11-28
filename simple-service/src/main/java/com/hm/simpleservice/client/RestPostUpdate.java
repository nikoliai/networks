package com.hm.simpleservice.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

public class RestPostUpdate {
	
	private String ipAddress;
	private String port;
	private Client client;
	
	public RestPostUpdate(String ipAddress, String port) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.client = ClientBuilder.newClient();
	}
	
	public void send(JSONObject message) {
		//TODO
		WebTarget target = client.target("http://127.0.0.1:80/");

		target.request(MediaType.APPLICATION_JSON).put(Entity.text(message.toJSONString()));
	}

}

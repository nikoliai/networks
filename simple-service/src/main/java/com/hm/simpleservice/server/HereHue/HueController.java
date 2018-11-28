package com.hm.simpleservice.server.HereHue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class HueController {

	private String baseURI = "/api/";
//	private String authorizedUsr = "197ea42c25303cef1a68c4042ed56887/";
	private String authorizedUsr = "newdeveloper/";
//	private String ipAddress = "localhost";
	private String ipAddress = "localhost";
	private String port = "80";
	
	private Client clientToHue;
	
	public HueController(String baseURI, String authorizedUsr, String ipAddress, String port) {
//		this.baseURI = baseURI;
//		this.authorizedUsr = authorizedUsr;
//		this.ipAddress = ipAddress;
//		this.port = port;
		clientToHue = ClientBuilder.newClient();
	}
	
	public String requestStatusAllLights() {
		WebTarget target = clientToHue.target("http://127.0.0.1:80/api/newdeveloper/lights");
		String return1 = target.request(MediaType.APPLICATION_JSON).get(String.class);
		
		return return1.toString();
	}
	
	public String requestStatusLight(int id) {
		WebTarget target = clientToHue.target("http://127.0.0.1:80/api/newdeveloper/lights/" + id + "/status");
		String return1 = target.request(MediaType.APPLICATION_JSON).get(String.class);

		return return1;
	}
	
	public String postTurnOfOn(int id, HueState newState) {
		WebTarget target = clientToHue.target("http://127.0.0.1:80/api/newdeveloper/lights/" + id + "/state");

		Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.json("{" + newState.getMessage() + "}"));
	
		return response.toString();
	}
	
	public String postNewColor(int id, HueColors newColor) {
		WebTarget target = clientToHue.target("http://127.0.0.1:80/api/newdeveloper/lights/" + id + "/state");

		Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.json("{" + newColor.getMessage() + "}"));
	
		return response.toString();
	}
	
	public enum HueColors {

		GREEN("\"sat\":254, \"bri\":254,\"hue\":20000"), ORANGE("\"sat\":254, \"bri\":254,\"hue\":4000"), RED("\"sat\":254, \"bri\":254,\"hue\":0000");
		
		private final String message;
		
		private HueColors(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
	
	public enum HueState {
		
		ON("\"on\":true"), OFF("\"on\":false");
		
		private final String message;
		
		private HueState(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}

	}
}

package com.hm.tfour.server.HereHue;

import java.io.Serializable;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Provides methods to change status and color of a HUE light.
 * 
 * @author Thilo Barth
 */
public class HueController implements Serializable {

	private String baseURI = "/api/";
//	private String authorizedUsr = "197ea42c25303cef1a68c4042ed56887/";
	private String authorizedUsr = "newdeveloper/";
//	private String ipAddress = "http://10.28.9.122";
	private String ipAddress = "http://127.0.0.1:80";
	
	private Client clientToHue;
	
	/**
	 * Initialize this controller.
	 */
	public HueController() {
		clientToHue = ClientBuilder.newClient();
	}
	
	/**
	 * Makes a put request to set state of the lamp with the committed id.
	 * 
	 * @param id of the lamp
	 * @param newState of the lamp
	 * @return response of the request
	 */
	public String postTurnOfOn(int id, HueState newState) {
		WebTarget target = clientToHue.target(ipAddress + baseURI + authorizedUsr + "lights/" + id + "/state");

		Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.json("{" + newState.getMessage() + "}"));
	
		return response.toString();
	}
	
	/**
	 * Makes a put request to change the color of the lamp with committed id.
	 * 
	 * @param id of lamp 
	 * @param newColor to change
	 * @return response of request
	 */
	public String postNewColor(int id, HueColors newColor) {
		WebTarget target = clientToHue.target(ipAddress + baseURI + authorizedUsr + "lights/" + id + "/state");

		Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.json("{" + newColor.getMessage() + "}"));
	
		return response.toString();
	}
	
	/**
	 * Color of a HUE lamp. Could Green Orange or Red
	 * 
	 * @author Thilo Barth
	 */
	public enum HueColors {

		GREEN("\"sat\":254, \"bri\":254,\"hue\":25000"), ORANGE("\"sat\":254, \"bri\":254,\"hue\":4000"), RED("\"sat\":254, \"bri\":254,\"hue\":1");
		
		private final String message;
		
		private HueColors(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
	
	/**
	 * State of a HUE lamp. Could be on or off
	 * 
	 * @author Thilo Barth
	 */
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

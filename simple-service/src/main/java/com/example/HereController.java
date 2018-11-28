package com.example;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HereController {

	private String requestGetCoordinatesBaseUri = "https://geocoder.api.here.com/6.2/geocode.json?app_id=%s&app_code=%s&searchtext=%s";
	private String requestGetTimeBaseUri = "https://route.api.here.com/routing/7.2/calculateroute.json?%s&app_code=%s%s";

	public HereController(String here_app_id, String here_app_code) {
		//TODO testen mit mehreren %s in base String und nur 1 oder 2 parameter
		requestGetCoordinatesBaseUri = String.format(requestGetCoordinatesBaseUri, here_app_id, here_app_code, "%s+%s+%s+%s");
		requestGetTimeBaseUri = String.format(requestGetTimeBaseUri, here_app_id, here_app_code, "&waypoint0=geo!%s,%s&waypoint1=geo!%s,%s&mode=fastest;car;traffic:disabled");
	}
	
	/**
	 * 
	 * @param houseNr1
	 * @param street1
	 * @param city1
	 * @param country1
	 * @param houseNr2
	 * @param street2
	 * @param city2
	 * @param country2
	 * @return time as string
	 */
	public String calculatingTravelTime(String houseNr1, String street1, String city1, String country1,
			String houseNr2,String street2, String city2, String country2) throws ParseException{

		Client client = ClientBuilder.newClient();

		// first request
		String response1 = sendCoordinatesGetRequest(client, houseNr1, street1, city1, country1);
		String[] coords1 = parseCoordinatesResponse(response1);

		// second request
		String response2 = sendCoordinatesGetRequest(client, houseNr2, street2, city2, country2);
		String[] coords2 = parseCoordinatesResponse(response2);

		// time request
		String responseTime = sendRoutCalculationGetRequest(client, coords1[0], coords1[1], coords2[0], coords2[1]);

		client.close();

		return parseTravelTimeResponse(responseTime);
	}
	
	public String calculatingTravelTime(String latStart, String lonStart, String latDes, String lonDes) throws ParseException{
		Client client = ClientBuilder.newClient();

		// time request
		String responseTime = sendRoutCalculationGetRequest(client, latStart, lonStart, latDes, lonDes);

		client.close();

		return parseTravelTimeResponse(responseTime);
	}

	public String[] sendCoordinatesGetRequest(String houseNr, String street, String city, String country) throws ParseException {
		Client client = ClientBuilder.newClient();
		String response = sendCoordinatesGetRequest(client, houseNr, street, city, country);
		client.close();
		return parseCoordinatesResponse(response);
	}

	private String sendCoordinatesGetRequest(Client client, String houseNr, String street, String city, String country) {
		//Create request
		String requestCoordinates = String.format(requestGetCoordinatesBaseUri, houseNr, street, city, country);

		//Get response
		return client.target(requestCoordinates).request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
	/**
	 * 
	 * @param client
	 * @param latStart
	 * @param longStart
	 * @param latFinish
	 * @param longFinish
	 * @return time as string
	 */
	private String sendRoutCalculationGetRequest(Client client, String latStart, String longStart, String latFinish, String longFinish) { 
		// create request
		String requestTime = String.format(requestGetTimeBaseUri, latStart, longStart, latFinish, longFinish);

		// get response
		return client.target(requestTime).request(MediaType.APPLICATION_JSON).get(String.class);
	}


	/**
	 * 
	 * 
	 * @param responseTT
	 * @return time as string
	 * @throws ParseException
	 */
	private String parseTravelTimeResponse(String responseTT) throws ParseException {
		JSONObject responseJson = parseResponseToJSON(responseTT);
		String time;
		try {
			time = ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) responseJson.get("response"))
					.get("route")).get(0)).get("summary")).get("travelTime").toString();

			// System.out.println("summary travel time: " + trTime);
		} catch (Exception e) {
			throw new ParseException(0, responseJson);
		}
		return time;
	}

	private String[] parseCoordinatesResponse(String response) throws ParseException {
		JSONObject responseJson = parseResponseToJSON(response);
		JSONObject coordinateJson;
		String[] requestedItems = new String[2];

		try {
			coordinateJson = ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONArray) ((JSONObject) responseJson
					.get("Response")).get("View")).get(0)).get("Result")).get(0)).get("Location"))
					.get("NavigationPosition")).get(0));
			requestedItems[0] =  coordinateJson.get("Latitude").toString();
			requestedItems[1] = coordinateJson.get("Longitude").toString();
		} catch (Exception e) {
			throw new ParseException(0, responseJson);
		}
		return requestedItems;
	}

	private JSONObject parseResponseToJSON(String response) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		Object obj;
		obj = jsonParser.parse(response);
		if (obj instanceof JSONObject) {
			return (JSONObject) obj;
		}
		return null;
	}
}

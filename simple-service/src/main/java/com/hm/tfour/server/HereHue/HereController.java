package com.hm.tfour.server.HereHue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * Class that represents connection to the "here" web service. Sends request to "here" web service to calculate travel time. 
 * @author Olga Nikoliai
 *
 */
public class HereController {

	private static String here_app_id = "lCp2io17mGlpmfZDl5jY";
	private static String here_app_code = "VW3eq70qhKoG9Qed65y9Uw";
	private static String requestGetCoordinatesBaseUri = "https://geocoder.api.here.com/6.2/geocode.json?app_id=%s&app_code=%s&searchtext=%s";
	private static String requestGetTimeBaseUri = "https://route.api.here.com/routing/7.2/calculateroute.json?app_id=%s&app_code=%s%s";
	
/**
 * Generates URI request to get coordinates
 * @return URI request 
 */
	private static String formatCoordinatesUri() {
		return String.format(requestGetCoordinatesBaseUri, here_app_id, here_app_code, "%s+%s+%s+%s");
	}
/**
 * Generates URI request to calculate travel time
 * @return URI request 
 */
	private static String formatTimeUri() {
		return String.format(requestGetTimeBaseUri, here_app_id, here_app_code, "&waypoint0=geo!%s,%s&waypoint1=geo!%s,%s&mode=fastest;car;traffic:disabled");
		}

	/**
	 * Connects to the web service to calculate travel time
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
	 * @throws UnsupportedEncodingException 
	 */
	public static String sendTravelTimeRequest(String houseNr1, String street1, String city1, String country1,
			String houseNr2,String street2, String city2, String country2) throws ParseException, UnsupportedEncodingException{

		Client client = ClientBuilder.newClient(); // client to communicate to the web service

		// first request
		String response1 = sendCoordinatesGetRequest(client, houseNr1, street1, city1, country1);
		String[] coords1 = parseCoordinatesResponse(response1);

		// second request
		String response2 = sendCoordinatesGetRequest(client, houseNr2, street2, city2, country2);
		String[] coords2 = parseCoordinatesResponse(response2);

		// time request
		String responseTime = sendRouteCalculationGetRequest(client, coords1[0], coords1[1], coords2[0], coords2[1]);

		client.close();

		return parseTravelTimeResponse(responseTime);
	}


	/**
	 * Sends request to the web service to receive coordinates from address
	 * 
	 * @param client 
	 * @param houseNr
	 * @param street
	 * @param city
	 * @param country
	 * @return response from the web server
	 * @throws UnsupportedEncodingException
	 */
	private static String sendCoordinatesGetRequest(Client client, String houseNr, String street, String city, String country) throws UnsupportedEncodingException {
		//Create request
		String requestCoordinates = String.format(formatCoordinatesUri(), URLEncoder.encode(houseNr, "UTF-8"), URLEncoder.encode(street, "UTF-8"), URLEncoder.encode(city, "UTF-8"), URLEncoder.encode(country, "UTF-8"));
		//Get response
		return client.target(requestCoordinates).request(MediaType.APPLICATION_JSON).get(String.class);
	}

	/**
	 * Sends request to the web service to calculate travel time 
	 * 
	 * @param client
	 * @param latStart latitude of the start position of a car 
	 * @param longStart longitude of the start position of a car 
	 * @param latFinish  latitude of the final position of a car
	 * @param longFinish longitude of the final position of a car
	 * @return time to get from start in seconds to finish as string
	 */
	private static String sendRouteCalculationGetRequest(Client client, String latStart, String longStart, String latFinish, String longFinish) { 
		// create request
		String requestTime = String.format(formatTimeUri(), latStart, longStart, latFinish, longFinish);

		// get response
		return client.target(requestTime).request(MediaType.APPLICATION_JSON).get(String.class);
	}


	/**
	 * 
	 * Gets travel time from the response of the web service
	 * @param responseTime response from the web service
	 * @return time as string
	 * @throws ParseException
	 */
	private static String parseTravelTimeResponse(String responseTime) throws ParseException {
		JSONObject responseJson = parseResponseToJSON(responseTime);
		String time;
		try {
			time = ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) responseJson.get("response"))
					.get("route")).get(0)).get("summary")).get("travelTime").toString();
		} catch (Exception e) {
			throw new ParseException(0, responseJson);
		}
		return time;
	}
	
/**
 * Parses the response from the web service to get coordinates 
 * @param response
 * @return latitude and longitude for the address requested by the web service
 * @throws ParseException
 */
	private static String[] parseCoordinatesResponse(String response) throws ParseException {
		JSONObject responseJson = parseResponseToJSON(response);
		JSONObject coordinateJson;
		String[] requestedItems = new String[2];

		try {
			coordinateJson = ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONArray) ((JSONObject) responseJson
					.get("Response")).get("View")).get(0)).get("Result")).get(0)).get("Location"))
					.get("NavigationPosition")).get(0));//get coordinates as JSONObject
			requestedItems[0] =  coordinateJson.get("Latitude").toString();
			requestedItems[1] = coordinateJson.get("Longitude").toString();
		} catch (Exception e) {
			throw new ParseException(0, responseJson);
		}
		return requestedItems;
	}
/**
 * Creates a JSON object from the response from the web service
 * @param response from the web service 
 * @return response from the web service as JSON object 
 * @throws ParseException
 */
	private static JSONObject parseResponseToJSON(String response) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		Object obj;
		obj = jsonParser.parse(response);
		if (obj instanceof JSONObject) {
			return (JSONObject) obj;
		}
		return null;
	}
}

package com.hm.tfour.server.ressource;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hm.tfour.server.ServerController;
import com.hm.tfour.server.HereHue.HereController;
import com.hm.tfour.server.model.Car;
import com.hm.tfour.server.model.CarUtil;

@Path("driver/state")
public class RessourceDriverState {

	public static final String PATH = "driver/state";

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String obtainNewDriverState(String jsonString) {
		JSONParser parser = new JSONParser();
		JSONObject jsonInput = null;
		try {
			jsonInput = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Car driver = CarUtil.findCar(ServerController.getCarList(), Integer.parseInt(jsonInput.get("id").toString()));
		
		//New state
		switch ((String) jsonInput.get("state")) {
		case "FREE": //Must be Car.State.Free.name()
			driver.setState(Car.State.FREE);
			return "200 Ok";
		case "INACTIVE":
			driver.setState(Car.State.INACTIVE);
			return "200 Ok";
		case "IN_TIME": //Else Car is on road
		case "LATE":
			Object jsonObj = jsonInput.get("location");
			if (jsonObj instanceof JSONArray) {
				JSONArray jsonLocationArray = (JSONArray) jsonObj;
				String[] newLocation = new String[4];
				for (int i = 0; i < newLocation.length; i++) {
					newLocation[i] = jsonLocationArray.get(i).toString();
				}

				//newLocation entries must not be null
				if (!newLocation[0].equals(null) && !newLocation[1].equals(null) && !newLocation[2].equals(null) && !newLocation[3].equals(null)) {
					try {
						String[] desAddress = driver.getDestinationAddress();

						//Get travel time in sec
						long newTravelTime = Long.parseLong(HereController.sendTravelTimeRequest(newLocation[0], newLocation[1], newLocation[2], newLocation[3], desAddress[0], desAddress[1], desAddress[2], desAddress[3]));

						long newStartTime = driver.getDestinationTime() - newTravelTime *1000;
						long currentTime = System.currentTimeMillis();

						if (newStartTime >= currentTime) {
							driver.setState(Car.State.IN_TIME);
						} else {
							driver.setState(Car.State.LATE);
						}

						driver.setStartAddress(newLocation);
						return "Ressource Rest Driver State: 200 Ok";
					} catch (ParseException | NumberFormatException | UnsupportedEncodingException  e) {
						return "Ressource Rest Driver State: 400 Wrong Json String";
					}
				}
			}
		}
		return "Ressource Rest Driver State: 400 Wrong Json String"; //200 ok could be only send in switch case
	}
}

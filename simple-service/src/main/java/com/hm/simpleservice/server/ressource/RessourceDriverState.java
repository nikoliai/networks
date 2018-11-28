package com.hm.simpleservice.server.ressource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hm.simpleservice.server.ServerController;
import com.hm.simpleservice.server.HereHue.HereController;
import com.hm.simpleservice.server.model.Car;
import com.hm.simpleservice.server.model.CarUtil;

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

		Car driver = CarUtil.findCar(ServerController.getArrayList(), Integer.parseInt(jsonInput.get("id").toString()));
		System.out.println("Driver ID: " + jsonInput.get("id"));

		switch ((String) jsonInput.get("state")) {
		case "FREE":
			driver.setState(Car.State.FREE);
			System.out.println("Driver state to Free");
			break;
		case "INACTIVE":
			driver.setState(Car.State.INACTIVE);
			System.out.println("Driver state to Inactinve");
			break;
		case "IN_TIME":
		case "LATE":
			Object jsonObj = jsonInput.get("location");
			if (jsonObj instanceof JSONArray) {
				JSONArray jsonLocationArray = (JSONArray) jsonObj;
				String[] newLocation = new String[4];
				for (int i = 0; i < newLocation.length; i++) {
					newLocation[i] = jsonLocationArray.get(i).toString();
				}

				if (!newLocation[0].equals(null) && !newLocation[1].equals(null) && !newLocation[2].equals(null) && !newLocation[3].equals(null)) {
					try {
						String[] desAddress = driver.getDestinationAddress();

						long newTravelTime = Long.parseLong(HereController.calculatingTravelTime(newLocation[0], newLocation[1], newLocation[2], newLocation[3], desAddress[0], desAddress[1], desAddress[2], desAddress[3])) * 1000;

						long newStartTime = driver.getDestinationTime() - newTravelTime;
						long currentTime = System.currentTimeMillis();
						
						if (newStartTime >= currentTime) {
							driver.setState(Car.State.IN_TIME);
							System.out.println("Driver state to InTime");
						} else {
							driver.setState(Car.State.LATE);
							System.out.println("Driver state to Late");
						}

						driver.setStartAddress(newLocation);
					} catch (ParseException e) {
						return "444 Wrong Json String";
					}
				} else {
					return "444 Wrong Json String";
				}
			} else {
				return "444 Wrong Json String";
			}
			break;
		}
		return "200 Ok";

	}
}

package com.ressource;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.HereController;
import com.hm.taxi.Car;

@Path("driver/state")
public
class RessourceDriverState {

	private ArrayList<Car> driverList;
	private HereController hereController;

//	public RessourceDriverState(ArrayList<Car> dirverList, HereController hereController) {
//		this.driverList = driverList;
//		this.hereController = hereController;
//	}

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
		
		//TODO find driver
		Car driver = null;
		System.out.println("Driver ID: " + jsonInput.get("id"));

		switch ((String) jsonInput.get("state")) {
		case "Free":
//			driver.setStatus(Car.Status.Free);
			System.out.println("Driver state to Free");
			break;
		case "Inactive":
//			driver.setStatus(Car.Status.Inactive);
			System.out.println("Driver state to Inactinve");
			break;
		case "InTime":
		case "Late":
			String[] newLocation = (String[]) jsonInput.get("location");

			if (newLocation != null && newLocation.length == 4 && !newLocation[0].equals(null) && !newLocation[1].equals(null) && !newLocation[2].equals(null) && !newLocation[3].equals(null)) {
				try {
					String[] newLocationCoords = hereController.sendCoordinatesGetRequest( newLocation[0], newLocation[1], newLocation[2], newLocation[3]);

					int newTravelTime = Integer.parseInt(hereController.calculatingTravelTime(newLocationCoords[0], newLocation[1], driver.getLatitudeDestination(), driver.getLongitudeDestination()));

					int newStartTime = driver.getDestinationTime() - newTravelTime;

					if (newStartTime >= System.currentTimeMillis()) {
//						driver.setStatus(Car.Status.InTime);
						System.out.println("Driver state to InTime");
					} else {
//						driver.setStatus(Car.Status.Late);
						System.out.println("Driver state to Late");
					}

//					driver.setLatitudeStart(newLocationCoords[0]);
//					driver.setLongitudeStart(newLocationCoords[1]);
//					driver.setTravelTime(newTravelTime);
				} catch (ParseException e) {
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

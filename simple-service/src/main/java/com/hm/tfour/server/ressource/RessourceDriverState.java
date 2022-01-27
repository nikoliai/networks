package com.hm.tfour.server.ressource;

import java.io.UnsupportedEncodingException;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hm.tfour.server.ServerController;
import com.hm.tfour.server.HereHue.HereController;
import com.hm.tfour.server.model.Car;
import com.hm.tfour.server.model.Car.State;
import com.hm.tfour.server.model.CarUtil;

@Path("driver/state")

/**
 * Class representing actual state of drivers
 * 
 * @author Olga Nikoliai
 *
 */
public class RessourceDriverState {

	public static final String PATH = "driver/state";

	@POST // calls the function after the post request
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	/**
	 * Changes the driver data according to the new information
	 * 
	 * @param jsonString new data posted on the own web service
	 * @return message if if was possible to change the state or not
	 */
	public String obtainNewDriverState(String jsonString) {
		System.out.println("I am here because smth was posted I suppose. Obtaining new driver state");
		JSONParser parser = new JSONParser();
		JSONObject jsonInput = null;
		try {
			jsonInput = (JSONObject) parser.parse(jsonString);//
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
// car by which the data should be changed
		Car driver = CarUtil.findCar(ServerController.getCarList(), Integer.parseInt(jsonInput.get("id").toString()));

		// New state
		switch ((String) jsonInput.get("state")) {
		case "FREE": // Must be Car.State.Free.name()
			driver.setState(Car.State.FREE);
			return "200 Ok";
		case "INACTIVE":
			driver.setState(Car.State.INACTIVE);
			return "200 Ok";
		case "IN_TIME": // the initial status is always in time, then it will be checked if the desired
						// time is realistic
		case "ON_ROAD": // calculate new travel time
			System.out.println("Checking travel time");
			Object jsonObj = jsonInput.get("location");
			if (jsonObj instanceof JSONArray) {
				JSONArray jsonLocationArray = (JSONArray) jsonObj;
				String[] newLocation = new String[4]; // get new location
				for (int i = 0; i < newLocation.length; i++) {
					newLocation[i] = jsonLocationArray.get(i).toString();
				}

				// newLocation entries must not be null
				if (!newLocation[0].equals(null) && !newLocation[1].equals(null) && !newLocation[2].equals(null)
						&& !newLocation[3].equals(null)) {
					try {
						String[] desAddress = driver.getDestinationAddress();

						// Get travel time in sec
						long newTravelTime = Long.parseLong(
								HereController.sendTravelTimeRequest(newLocation[0], newLocation[1], newLocation[2],
										newLocation[3], desAddress[0], desAddress[1], desAddress[2], desAddress[3]));

						long diffDesiredTimeAndCalculatedTravTime = driver.getDestinationTime() - newTravelTime * 1000;
						long currentTime = System.currentTimeMillis();

						if (diffDesiredTimeAndCalculatedTravTime >= currentTime) {
							driver.setState(Car.State.IN_TIME);
							System.out.println("Comming in time");
						} else {
							driver.setState(Car.State.LATE);
							System.out.println("Being late");

						}

						driver.setStartAddress(newLocation); // change the start address by the driver
						return "Ressource Rest Driver State: 200 Ok";
					} catch (ParseException | NumberFormatException | UnsupportedEncodingException e) {
						return "Ressource Rest Driver State: 400 Wrong Json String";
					}
				}
			}
		}
		return "Ressource Rest Driver State: 400 Wrong Json String"; // 200 ok could be only send in switch case
	}

private static RessourceDriverState getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@GET // calls the function after the post request
	@Produces(MediaType.TEXT_PLAIN)

	public String getNewDriverState() {
		return "Hello World";

//	}
	
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
////	@Path("retrieve/{uuid}")
//	public DriverState driverState() {
//	    return new MyJaxbBean("Agamemnon", 32);
	}
//	public Response retrieveSomething(@PathParam("uuid") String uuid) {
//	    if(uuid == null || uuid.trim().length() == 0) {
//	        return Response.serverError().entity("UUID cannot be blank").build();
//	    }
//	    Entity entity = service.getById(uuid);
//	    if(entity == null) {
//	        return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + uuid).build();
//	    }
//	    String json = //convert entity to json
//	    return Response.ok(json, MediaType.APPLICATION_JSON).build();
//	}
}

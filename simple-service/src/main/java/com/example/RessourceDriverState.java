package com.example;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.parser.ParseException;

import com.hm.taxi.Car;

@Path("driver/state/{jsonString}")
class RessourceDriverState {

	private ArrayList<Car> driverList;
	private HereController hereController;

	public RessourceDriverState(ArrayList<Car> dirverList, HereController hereController) {
		this.driverList = driverList;
		this.hereController = hereController;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String obtainNewDriverState(DriverStatusPost jsonString) {
		//TODO find driver
		Car driver = null;

		switch (jsonString.getState()) {
		case Free:
			driver.setStatus(Car.Status.Free);
			break;
		case Inactive:
			driver.setStatus(Car.Status.Inactive);
			break;
		}

		String[] newLocation = jsonString.getNewLocation();

		if (newLocation != null && newLocation.length == 4 && !newLocation[0].equals(null) && !newLocation[1].equals(null) && !newLocation[2].equals(null) && !newLocation[3].equals(null)) {
			try {
				String[] newLocationCoords = hereController.sendCoordinatesGetRequest( newLocation[0], newLocation[1], newLocation[2], newLocation[3]);

				int newTravelTime = Integer.parseInt(hereController.calculatingTravelTime(newLocationCoords[0], newLocation[1], driver.getLatitudeDestination(), driver.getLongitudeDestination()));

				int newStartTime = driver.getDestinationTime() - newTravelTime;

				if (newStartTime >= System.currentTimeMillis()) {
					driver.setStatus(Car.Status.InTime);
				} else {
					driver.setStatus(Car.Status.Late);
				}

				driver.setLatitudeStart(newLocationCoords[0]);
				driver.setLongitudeStart(newLocationCoords[1]);
				driver.setTravelTime(newTravelTime);
			} catch (ParseException e) {
				return "444 Wrong Json String";
			}
		} else {
			return "444 Wrong Json String";
		}

		return "200 Ok";

	}
}

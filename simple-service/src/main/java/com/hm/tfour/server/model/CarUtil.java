package com.hm.tfour.server.model;

import java.util.ArrayList;

/**
 * Represents some util methods for the class Car
 * 
 * @author Thilo Barth
 * @author Olga Nikoliai
 */
public class CarUtil {
	
	/**
	 * Creates a json String for the T4 program Rest ressource to post new driver state.
	 * If the new state is FREE or INACTIVE the entries of newLocation could be null.
	 * The newLocation array must have 4 entries in this order: houseNr, street, city, country.
	 * 
	 * @param id of Car
	 * @param newState the new state that should be posted.
	 * @param newLocation must not be null
	 * @return json String for Post request
	 * @throws IllegalArgumentException if driver state or newLocation 
	 */
	public static String createDriverStatePostRequestJsonString(int id, Car.State newState, String[] newLocation) throws IllegalArgumentException {
		if (newState != null && newLocation != null && newLocation.length == 4) {
			return "{\n\"id\":" + id + ",\n\"state\":\"" + newState.name() + "\",\n\"location\":[\"" + newLocation[0] + "\", \"" + newLocation[1] + "\", \"" + newLocation[2] + "\", \"" + newLocation[3] + "\"]}";
		}
		throw new IllegalArgumentException("Driver State or wrong new location.");
	}
	
	/**
	 * Finds the car in the committed list with the committed id.
	 * 
	 * @param carList the list to be searched in
	 * @param id to be searched
	 * @return the car if found
	 * @throws IllegalArgumentException if the driver id was not found.
	 */
	public static Car findCar(ArrayList<Car> carList, int id) throws IllegalArgumentException {
		for (int i = 0; i < carList.size(); i++) {
			if (carList.get(i).getID()  == id) {
				return carList.get(i);
			}
		}
		throw new IllegalArgumentException("Driver-ID: " + id + " not found.");
	}

}

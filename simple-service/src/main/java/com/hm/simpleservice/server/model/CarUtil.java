package com.hm.simpleservice.server.model;

import java.util.ArrayList;

public class CarUtil {
	
	public static String getPostRequestJsonString(int id, Car.State newState, String[] newLocation) {
		return "{\n\"id\":" + id + ",\n\"state\":\"" + newState.name() + "\"\n\"location\":[\"" + newLocation[0] + "\", \"" + newLocation[1] + "\", \"" + newLocation[2] + "\", \"" + newLocation[3] + "\"]}";
	}
	
	public static Car findCar(ArrayList<Car> carList, int id) {
		for (int i = 0; i < carList.size(); i++) {
			if (carList.get(i).getID()  == id) {
				return carList.get(i);
			}
		}
		return null;
	}

}

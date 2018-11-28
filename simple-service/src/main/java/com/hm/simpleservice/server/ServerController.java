package com.hm.simpleservice.server;

import java.net.URI;
import java.util.ArrayList;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.hm.simpleservice.server.HereHue.HueController;
import com.hm.simpleservice.server.HereHue.HueController.HueColors;
import com.hm.simpleservice.server.model.Car;

public class ServerController {

	public final static String BASE_URI = "http://localhost:8080/myapp/";

	private static ArrayList<Car> driverList;
	private final HttpServer ressourceController;
	private HueController hueController;

	public ServerController(ArrayList<Car> driverList) {
		this.driverList = driverList;
		hueController = new HueController("", "", "", "");
		updateLamps();
		Thread carUpdater = new Thread(new CarUpdater());
		carUpdater.start();
		ressourceController = startRessourceController();
	}

	public static ArrayList<Car> getArrayList() {
		return driverList;
	}

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startRessourceController() {
		// create a resource config that scans for JAX-RS resources and providers
		// in com.example package
		final ResourceConfig rc = new ResourceConfig().packages("com.hm.simpleservice.server.ressource");

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	private void updateLamps() {
		for (int i = 0; i < driverList.size(); i++) {
			Car car = driverList.get(i);
			switch (car.getState()) {
			case INACTIVE:
				hueController.postTurnOfOn(i + 1, HueController.HueState.OFF);
				break;
			case FREE:
				hueController.postTurnOfOn(i + 1, HueController.HueState.ON);
				hueController.postNewColor(i + 1, HueColors.ORANGE);
				break;
			case IN_TIME:
				hueController.postTurnOfOn(i + 1, HueController.HueState.ON);
				hueController.postNewColor(i + 1, HueColors.GREEN);
				break;

			case LATE:
				hueController.postTurnOfOn(i + 1, HueController.HueState.ON);
				hueController.postNewColor(i + 1, HueColors.RED);
				break;

			default:
				break;
			}
		}
	}

	public Car addCar(String[] startAddress, String[] targetAddress, long destinationTime) {
		Car car = new Car(findNewId(0, 0) + 1, startAddress, targetAddress, destinationTime);
		driverList.add(car);
		return car;
	}

	private int findNewId(int currentIndex, int currentNewIndex) {
		if (currentIndex == driverList.size()) {
			return currentNewIndex;
		}		
		if (driverList.get(currentIndex).getID() == currentNewIndex + 1) {
			return findNewId(0, currentNewIndex + 1);
		} else {
			return findNewId(currentIndex + 1, currentNewIndex);
		}
	}

	public void delDriver(int id) {
		if (id < driverList.size() && id >= 0) {
			driverList.remove(id);
		}
	}

	public Car getDriver(int id) {
		for (Car driver : driverList) {
			if (driver.getID() == id) {
				return driver;
			}
		}
		return null;
	}

	class CarUpdater implements Runnable {

		@Override
		public void run() {
			try {
				while(true) {
					updateLamps();
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

package com.hm.tfour.server;

import java.net.URI;
import java.util.ArrayList;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.hm.tfour.server.HereHue.HueController;
import com.hm.tfour.server.HereHue.HueController.HueColors;
import com.hm.tfour.server.model.Car;
import com.hm.tfour.server.model.CarUtil;

/**
 * The controller of the server.
 * 
 * @author Thilo Barth
 */
public class ServerController {

	public final static String BASE_URI = "http://localhost:8080/myapp/";
	public final static String RESSOURCE_PACKAGE = "com.hm.tfour.server.ressource";

	private static ArrayList<Car> carList = new ArrayList<>();
	private HueController hueController;

	/**
	 * Initialize the ServerController
	 * 
	 * @param driverList the list of all cars
	 */
	public ServerController(ArrayList<Car> driverList) {
		this.carList = driverList;
		hueController = new HueController();
		Thread carUpdater = new Thread(new CarLampUpdater());
		carUpdater.start(); //Start HUE lamp update
		startRessourceController(); //Start http server
	}

	/**
	 * @return list of all cars.
	 */
	public static ArrayList<Car> getCarList() {
		return carList;
	}

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 * @return Grizzly HTTP server.
	 */
	private HttpServer startRessourceController() {
		// create a resource config that scans for JAX-RS resources and providers
		// in com.example package
		final ResourceConfig rc = new ResourceConfig().packages(RESSOURCE_PACKAGE);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	/**
	 * Adds a new Car to the list. Addresses has to be {"houseNr", "street", "city", "country"}
	 * 
	 * @param startAddress the address the car starts
	 * @param targetAddress the address the car ends
	 * @param destinationTime the time when the car should reached the destination
	 * @return
	 */
	public Car addCar(String[] startAddress, String[] targetAddress, long destinationTime) {
		Car car = new Car(findNewId(0, 0) + 1, startAddress, targetAddress, destinationTime);
		carList.add(car);
		return car;
	}

	/**
	 * Finds the next free id.
	 * 
	 * @param currentIndex current position in list
	 * @param currentNewIndex the start of searching the next free id
	 * @return
	 */
	private int findNewId(int currentIndex, int currentNewIndex) {
		if (currentIndex == carList.size()) {
			return currentNewIndex; //All drivers where checked
		}		
		if (carList.get(currentIndex).getID() == currentNewIndex + 1) {
			return findNewId(0, currentNewIndex + 1); //Current driver has the currentNewId, so look for next id
		} else {
			return findNewId(currentIndex + 1, currentNewIndex); //Look for next driver in list
		}
	}

	/**
	 * Removes the car with the committed id.
	 * 
	 * @param id of the car to be deleted.
	 * @return true if car was deleted otherwise false
	 */
	public boolean removeCar(int id) {
		if (id < carList.size() && id >= 0) {
			carList.remove(id);
			return true;
		}
		return false;
	}

	/**
	 * Returns the car with the committed id.
	 * 
	 * @param id of the car
	 * @return instance of Car
	 */
	public Car getCar(int id) {
		for (Car driver : carList) {
			if (driver.getID() == id) {
				return driver;
			}
		}
		return null;
	}

	/**
	 * Runnable class to update the lamps every 5 sec
	 * @author Thilo Barth
	 */
	class CarLampUpdater implements Runnable {

		private ArrayList<RedBlinker> blinkerList;

		/**
		 * Initialize the CarLampUpdater.
		 */
		public CarLampUpdater() {
			//Creates for every car a blinker
			this.blinkerList = new ArrayList<>();
			for (int i = 1; i <= carList.size(); i++) {
				this.blinkerList.add(new RedBlinker(i));
			}
		}

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

		/**
		 * Updates all HUE lamps.
		 */
		private void updateLamps() {
			for (int i = 1; i <= carList.size(); i++) {
				Car car = CarUtil.findCar(carList, i);
				switch (car.getState()) {
				case INACTIVE:
					blinkerList.get(i - 1).setIsRunning(false);
					blinkerList.set(i - 1, new RedBlinker(i));
					hueController.postTurnOfOn(i, HueController.HueState.OFF);
					break;
				case FREE:
					blinkerList.get(i - 1).setIsRunning(false);
					blinkerList.set(i - 1, new RedBlinker(i));
					hueController.postTurnOfOn(i, HueController.HueState.ON);
					hueController.postNewColor(i, HueColors.ORANGE);
					break;
				case IN_TIME:
					blinkerList.get(i - 1).setIsRunning(false);
					blinkerList.set(i - 1, new RedBlinker(i));
					hueController.postTurnOfOn(i, HueController.HueState.ON);
					hueController.postNewColor(i, HueColors.GREEN);
					break;

				case LATE:
					RedBlinker blinker = blinkerList.get(i - 1);
					if (blinker.getIsStarted() == false) {
						new Thread(blinker).start();
					}
					break;
				}
			}
		}

		/**
		 * A runnable to let the a HUE lamp blink.
		 * 
		 * @author Thilo Barth
		 */
		private class RedBlinker implements Runnable {

			private int i;
			private boolean isRunning = true;
			private boolean isStarted = false;

			/**
			 * Initialize this blinker.
			 * @param i the id of the lamp
			 */
			public RedBlinker(int i) {
				this.i = i;
			}

			@Override
			public void run() {
				try {
					this.isStarted = true;
					while (isRunning) {
						hueController.postTurnOfOn(i, HueController.HueState.ON);
						hueController.postNewColor(i, HueColors.RED);
						Thread.sleep(1000);
						hueController.postTurnOfOn(i, HueController.HueState.OFF);
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * Sets the isRunning flag. Set it to false to stop the thread.
			 * 
			 * @param isRunning
			 */
			public void setIsRunning(boolean isRunning) {
				this.isRunning = isRunning;
			}
			
			/**
			 * @return isStarted
			 */
			public boolean getIsStarted() {
				return this.isStarted;
			}

		}

	}
}

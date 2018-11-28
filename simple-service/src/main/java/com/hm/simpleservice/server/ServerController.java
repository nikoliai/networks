package com.hm.simpleservice.server;

import java.util.ArrayList;

import com.hm.simpleservice.server.HereHue.HueController;
import com.hm.simpleservice.server.model.Car;

public class ServerController {

	private ArrayList<Car> driverList;
	private HueController hueController;

	public ServerController(ArrayList<Car> driverList) {
		this.driverList = driverList;
		hueController = new HueController("", "", "", "");
		//TODO HERE Controller
		//TODO Rest Controller
		updateLamps();
		Thread carUpdater = new Thread(new CarUpdater());
		carUpdater.start();
	}

	private void updateLamps() {
		//TODO update lamp with driverstate
	}

	public Car addCar(String startAddress, String targetAddress) {
		Car car = new Car(findNewId(0, 0) + 1, startAddress, targetAddress);
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
					for (Car driver : driverList) {
						int driverStartTime = driver.getStartTime();
						driver.updateTime();
						if (driverStartTime < driver.getStartTime()) {
							driver.setState(Car.State.LATE);
						}
						updateLamps();
					}
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

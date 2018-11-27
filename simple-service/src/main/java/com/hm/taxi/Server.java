package com.hm.taxi;

import java.io.Serializable;

import org.json.simple.JSONObject;

import com.hm.taxi.Car.Status;

public class Server implements Serializable {

    public Server() {
        JSONObject myObject = new JSONObject();
        myObject.put("id", "car2");
        myObject.put("status", "Inactive");
        JSONObject location = new JSONObject();
        location.put("lat", 10);
        location.put("lon", 20);
        myObject.put("location", location);
        System.out.println(myObject);

        Car car1 = new Car("car1", "100", "200", "200", "300");
        Car car2 = new Car("car2", "100", "200", "200", "300");
        Car car3 = new Car("car3", "100", "200", "200", "300");

        Car[] cars = new Car[] { car1, car2, car3 };
        System.out.println("car2 before:" + car2.toString());
        this.changeStatus(cars, myObject);
        System.out.println("car2 after:" + car2.toString());

    }

    // public class SendRequest {
    // public Status status;
    // String statusId;
    // public SendRequest (String statusId, Status status) {
    // this.status = status;
    // this. statusId = statusId;
    //
    // }
    // public SendRequest() {
    //
    // }
    //
    // };

    public void changeStatus(Car[] cars, JSONObject changedStatus) {
        JSONObject location = (JSONObject) changedStatus.get("location");

        String lat = location.get("lat").toString();
        String lon = location.get("lon").toString();
        for (Car car : cars) {
            if (car.getId().equals(changedStatus.get("id").toString())) {
                car.setLatitudeStart(lat);
                car.setLongitudeStart(lon);
                String status = changedStatus.get("status").toString();
                car.setStatus(status);
            }
        }

    }
}

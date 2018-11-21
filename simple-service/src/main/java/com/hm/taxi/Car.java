package com.hm.taxi;

public class Car {
    String latitude;
    String longitude;
    Status status;

    Car(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = Status.IN_TIME;
    }

    public enum Status {
        INACTIVE, FREE, IN_TIME, LATE
    };

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    Car car1 = new Car("500", "400");
    Car car2 = new Car("200", "600");
    Car car3 = new Car("1000", "2000");

}

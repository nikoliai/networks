package com.hm.taxi;


public class Car {
    String latitudeStart;
    String longitudeStart;
    String latitudeFinish;
    String longigudeFinish;
    Status status;
    

    Car(String originalAddress, String targetAddress) {
       
        this.status = Status.IN_TIME;
    }
    
    public enum Status {
        INACTIVE, FREE, IN_TIME, LATE
    };

    public void setLatitude(String latitude) {
        this.latitudeStart = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitudeStart = longitude;
    }

    public void setStatus(Status status) {
        this.status = status;
        
    }

    Car car1 = new Car("500", "400");
    Car car2 = new Car("200", "600");
    Car car3 = new Car("1000", "2000");

}

package com.ankit_1107.project.bookyourcar;


public class City {
    private String name;
    double latitide,longitude;

    public City(){}

    public City(String name, double latitide, double longitude) {
        this.name = name;
        this.latitide = latitide;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitide() {
        return latitide;
    }

    public void setLatitide(double latitide) {
        this.latitide = latitide;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

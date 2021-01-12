package com.ankit_1107.project.bookyourcar;

import java.io.Serializable;
import java.util.List;

public class Car implements Serializable {
    private String carId;
    private String ownerId;
    private String brandName;
    private String modelName;
    private String vehicleNumber;
    private String fuelType;
    private int seatCount;
    private String city;
    private Double locationLatitude;
    private Double locationLongitude;
    private List<String> bookingHistoryCar;
    private int price;
    private Boolean checkAC;
    private Boolean activeStatus;
    private String lendCarUri;
    private String ownerPhoneNumber;
    private List<Long> bookedNoDate;

    public Car(){}

    public Car(String carId, String ownerId, String brandName, String modelName, String vehicleNumber, String fuelType, int seatCount, String city, Double locationLatitude, Double locationLongitude, List<String> bookingHistoryCar, int price, Boolean checkAC, Boolean activeStatus, String lendCarUri, String ownerPhoneNumber, List<Long> bookedNoDate) {
        this.carId = carId;
        this.ownerId = ownerId;
        this.brandName = brandName;
        this.modelName = modelName;
        this.vehicleNumber = vehicleNumber;
        this.fuelType = fuelType;
        this.seatCount = seatCount;
        this.city = city;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.bookingHistoryCar = bookingHistoryCar;
        this.price = price;
        this.checkAC = checkAC;
        this.activeStatus = activeStatus;
        this.lendCarUri = lendCarUri;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.bookedNoDate = bookedNoDate;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public List<String> getBookingHistoryCar() {
        return bookingHistoryCar;
    }

    public void setBookingHistoryCar(List<String> bookingHistoryCar) {
        this.bookingHistoryCar = bookingHistoryCar;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getCheckAC() {
        return checkAC;
    }

    public void setCheckAC(Boolean checkAC) {
        this.checkAC = checkAC;
    }

    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getLendCarUri() {
        return lendCarUri;
    }

    public void setLendCarUri(String lendCarUri) {
        this.lendCarUri = lendCarUri;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public List<Long> getBookedNoDate() {
        return bookedNoDate;
    }

    public void setBookedNoDate(List<Long> bookedNoDate) {
        this.bookedNoDate = bookedNoDate;
    }
}


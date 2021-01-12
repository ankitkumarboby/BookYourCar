package com.ankit_1107.project.bookyourcar;

public class Booking {
    private String bookingId;
    private String userId; // UserId of the person who booked the car
    private String carId; //Car that is booked
    private String dateFrom;
    private String toDate;
    private Long noDateFrom;
    private Long noDateTo;
    private int amountPaid;
    private boolean isDriver;
    private int status;  //0->requested  1->current  2->completed 3->rejected
    private String userPhoneNumber;
    private String userName;
    private String vehicleNumber;
    private String ownerPhoneNumber;
    private String personPhotoUri;
    private String carPhotoUri;
    private String getCarFullName;
    private String ownerId;

    public Booking(){}

    public Booking(String bookingId, String userId, String carId, String dateFrom, String toDate, Long noDateFrom, Long noDateTo, int amountPaid, boolean isDriver, int status, String userPhoneNumber, String userName, String vehicleNumber, String ownerPhoneNumber, String personPhotoUri, String carPhotoUri, String getCarFullName, String ownerId) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.carId = carId;
        this.dateFrom = dateFrom;
        this.toDate = toDate;
        this.noDateFrom = noDateFrom;
        this.noDateTo = noDateTo;
        this.amountPaid = amountPaid;
        this.isDriver = isDriver;
        this.status = status;
        this.userPhoneNumber = userPhoneNumber;
        this.userName = userName;
        this.vehicleNumber = vehicleNumber;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.personPhotoUri = personPhotoUri;
        this.carPhotoUri = carPhotoUri;
        this.getCarFullName = getCarFullName;
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getGetCarFullName() {
        return getCarFullName;
    }

    public void setGetCarFullName(String getCarFullName) {
        this.getCarFullName = getCarFullName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Long getNoDateFrom() {
        return noDateFrom;
    }

    public void setNoDateFrom(Long noDateFrom) {
        this.noDateFrom = noDateFrom;
    }

    public Long getNoDateTo() {
        return noDateTo;
    }

    public void setNoDateTo(Long noDateTo) {
        this.noDateTo = noDateTo;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public String getPersonPhotoUri() {
        return personPhotoUri;
    }

    public void setPersonPhotoUri(String personPhotoUri) {
        this.personPhotoUri = personPhotoUri;
    }

    public String getCarPhotoUri() {
        return carPhotoUri;
    }

    public void setCarPhotoUri(String carPhotoUri) {
        this.carPhotoUri = carPhotoUri;
    }
}

package com.ankit_1107.project.bookyourcar;

import java.util.List;

public class Person {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String mainPhotoUri;
    private long dob;
    List<String> bookingHistoryPerson;
    List<String> carsOwnedByPerson;

    public Person(){}

    public Person(String userId, String name, String email, String password, String phoneNumber, String mainPhotoUri, long dob, List<String> bookingHistoryPerson, List<String> carsOwnedByPerson) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.mainPhotoUri = mainPhotoUri;
        this.dob = dob;
        this.bookingHistoryPerson = bookingHistoryPerson;
        this.carsOwnedByPerson = carsOwnedByPerson;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMainPhotoUri() {
        return mainPhotoUri;
    }

    public void setMainPhotoUri(String mainPhotoUri) {
        this.mainPhotoUri = mainPhotoUri;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public List<String> getBookingHistoryPerson() {
        return bookingHistoryPerson;
    }

    public void setBookingHistoryPerson(List<String> bookingHistoryPerson) {
        this.bookingHistoryPerson = bookingHistoryPerson;
    }

    public List<String> getCarsOwnedByPerson() {
        return carsOwnedByPerson;
    }

    public void setCarsOwnedByPerson(List<String> carsOwnedByPerson) {
        this.carsOwnedByPerson = carsOwnedByPerson;
    }



}

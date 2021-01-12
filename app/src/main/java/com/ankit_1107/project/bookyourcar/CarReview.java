package com.ankit_1107.project.bookyourcar;

public class CarReview {
    private String reviewId;
    private String userId;
    private String carId;
    private float rating;
    private String date,month,year;
    private String review;

    public CarReview(){}

    public CarReview(String reviewId, String userId, String carId, float rating, String date, String month, String year, String review) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.carId = carId;
        this.rating = rating;
        this.date = date;
        this.month = month;
        this.year = year;
        this.review = review;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

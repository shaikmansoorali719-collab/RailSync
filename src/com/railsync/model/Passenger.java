package com.railsync.model;

public class Passenger {
    private String passengerId;
    private String passengerName;
    private int age;
    private String gender;

    public Passenger(String passengerId, String passengerName, int age, String gender) {
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return passengerId + " | " + passengerName + " | Age: " + age + " | Gender: " + gender;
    }
}
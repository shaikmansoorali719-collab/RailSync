package com.railsync.model;

public class Ticket {
    private String ticketId;
    private String passengerId;
    private String trainNumber;
    private String source;
    private String destination;
    private String status;

    public Ticket(String ticketId, String passengerId, String trainNumber, String source, String destination, String status) {
        this.ticketId = ticketId;
        this.passengerId = passengerId;
        this.trainNumber = trainNumber;
        this.source = source;
        this.destination = destination;
        this.status = status;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ticketId + " | Passenger: " + passengerId + " | Train: " + trainNumber
                + " | " + source + " -> " + destination + " | Status: " + status;
    }
}
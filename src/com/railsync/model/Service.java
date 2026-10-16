package com.railsync.model;

public class Service {
    private String serviceId;
    private String serviceName;
    private String trainNumber;
    private String serviceType;
    private String status;

    public Service(String serviceId, String serviceName, String trainNumber, String serviceType, String status) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.trainNumber = trainNumber;
        this.serviceType = serviceType;
        this.status = status;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return serviceId + " | " + serviceName + " | Train: " + trainNumber
                + " | Type: " + serviceType + " | Status: " + status;
    }
}
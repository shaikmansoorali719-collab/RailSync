package com.railsync.model;

public class Station {
    private String stationId;
    private String stationName;
    private String city;
    private int platforms;
    private String zone;

    public Station(String stationId, String stationName, String city, int platforms, String zone) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.city = city;
        this.platforms = platforms;
        this.zone = zone;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public String getCity() {
        return city;
    }

    public int getPlatforms() {
        return platforms;
    }

    public String getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return stationId + " | " + stationName + " | " + city + " | Platforms: " + platforms + " | " + zone;
    }
}
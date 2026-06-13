package com.smartlogix.envios.dto;

public class Load {
    private String id;
    private String requiredVehicleType;
    private double weight;
    private double originLatitude;
    private double originLongitude;

    public Load() {
    }

    public Load(String id, String requiredVehicleType, double weight, double originLatitude, double originLongitude) {
        this.id = id;
        this.requiredVehicleType = requiredVehicleType;
        this.weight = weight;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequiredVehicleType() {
        return requiredVehicleType;
    }

    public void setRequiredVehicleType(String requiredVehicleType) {
        this.requiredVehicleType = requiredVehicleType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(double originLongitude) {
        this.originLongitude = originLongitude;
    }
}

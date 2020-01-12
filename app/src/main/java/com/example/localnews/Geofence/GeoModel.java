package com.example.localnews.Geofence;

public class GeoModel {
    Double latitude;
    Double longitude;

    public GeoModel(){}

    public GeoModel(Double lat, Double lang) {
        this.latitude = lat;
        this.longitude = lang;
    }
    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}

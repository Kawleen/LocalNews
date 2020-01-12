package com.example.localnews.Interface;

import com.example.localnews.Geofence.GeoModel;

import java.util.List;

public interface OnLoadLocationListner {
    void onLoadLocationSuccess(List<GeoModel> latlngs);
    void onLoadLocationFailure(String message);
}

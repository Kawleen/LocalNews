package com.example.localnews.Geofence;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import android.location.LocationListener;
import android.widget.Toast;

public class GPS_Service extends Service implements LocationListener {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private LocationManager m_locationManager;

    LocationList locationList;

    @Override
    public void onCreate() {
        this.m_locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Toast.makeText(getApplicationContext(), "Location Service starts", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.m_locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        this.m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1000, this);
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    public static Double[]  getCurrentLocation(Context c){
        LocationManager m_locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        Location location = m_locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Double[] loc = {location.getLatitude(), location.getLongitude()};
        return loc;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null)
            return;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}

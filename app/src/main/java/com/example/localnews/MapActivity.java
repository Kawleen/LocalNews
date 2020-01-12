package com.example.localnews;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.localnews.Geofence.GPS_Service;
import com.example.localnews.Geofence.GeoModel;
import com.example.localnews.Interface.OnLoadLocationListner;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryDataEventListener, GeoQueryEventListener, OnLoadLocationListner {

    private GoogleMap mMap;
    private Marker marker;
    private double currentLat;
    private double currentLong;

    private double latitutude;
    private double longitude;

    private DatabaseReference databaseReference;
    private DatabaseReference geoAreas;
    private GeoFire geoFire;

    List<GeoModel> geofenceList;
    List<LatLng> myAreaList;
    Toolbar toolbar;
    Button addGeoFence;
    private OnLoadLocationListner listner;

    FirebaseUser currentFirebaseUser;

    GeoQuery geoqery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        geoAreas = FirebaseDatabase.getInstance().getReference("GeoAreas").child("MyAreas");

        addGeoFence = (Button) findViewById(R.id.btnGeofence);
        addGeoFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double[] location = GPS_Service.getCurrentLocation(MapActivity.this);
                latitutude = location[0];
                longitude = location[1];
                AddGeofence(latitutude, longitude);
            }
        });


        toolbar = (Toolbar) findViewById(R.id.mapToolbar);
        toolbar.inflateMenu(R.menu.map_options);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.geoList)
                {
                    Intent fav = new Intent(MapActivity.this, GeoFence.class);
                    startActivity(fav);
                }
                return false;
            }
        });

        currentLat = -34;
        currentLong = 151;

        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                Double[] location = GPS_Service.getCurrentLocation(MapActivity.this);
                latitutude = location[0];
                longitude = location[1];
                if(latitutude != currentLat || longitude != currentLong){
                    if(mMap != null){
                        currentLat = latitutude;
                        currentLong = longitude;
                        addMarker(new LatLng(latitutude,longitude));
                    }
                }

                ha.postDelayed(this, 1000);
            }
        }, 1000);

        databaseReference = FirebaseDatabase.getInstance().getReference("GeoLocations");
        initArea();
        settingGeoFire();
    }


    private void initArea() {
        listner = this;
        geofenceList = new ArrayList<>();

        geoAreas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot linkSnapshot : dataSnapshot.getChildren()){
                    GeoModel model = linkSnapshot.getValue(GeoModel.class);
                    geofenceList.add(model);
                }

                listner.onLoadLocationSuccess(geofenceList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void settingGeoFire() {
        geoFire = new GeoFire(databaseReference);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        addCircle(myAreaList);
    }

    public void addMarker(final LatLng latLng){
        geoFire.setLocation("You", new GeoLocation(latitutude, longitude), new GeoFire.CompletionListener() {
        @Override
        public void onComplete(String key, DatabaseError error) {
            if(marker != null)marker.remove();

            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng).title("You"));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),12.0f));

        }
    });}

    public void addCircle(List<LatLng> myAreaList){
        if(geoqery != null){
            geoqery.removeGeoQueryEventListener((GeoQueryEventListener) MapActivity.this);
            geoqery.removeAllListeners();
        }

        for(LatLng m : myAreaList){
            mMap.addCircle(new CircleOptions().center(new LatLng(m.latitude,m.longitude))
                    .radius(500)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(5.0f));

            geoqery = geoFire.queryAtLocation(new GeoLocation(m.latitude,m.longitude),0.5f);
            geoqery.addGeoQueryEventListener(MapActivity.this);
        }


    }

    @Override
    public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
    }

    @Override
    public void onDataExited(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

    }

    @Override
    public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("GeoFence",String.format("%s entered Geofence",key));
    }

    @Override
    public void onKeyExited(String key) {
        sendNotification("GeoFence",String.format("%s left Geofence",key));
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        sendNotification("GeoFence",String.format("%s moving within Geofence Geofence",key));
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Toast.makeText(this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
    }


    private void sendNotification(String title, String content) {
        String NOTIFICATION_CHANNEL_ID = "Geofence";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My Notificaton",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(),notification);
    }

    private void AddGeofence(Double latitutude, Double longitude){
        GeoModel link =  new GeoModel(latitutude,longitude);
        String id = geoAreas.push().getKey();
        geoAreas.child(id).setValue(link);
        Toast.makeText(this,"Geofence Added.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadLocationSuccess(List<GeoModel> latlngs) {
        myAreaList = new ArrayList<>();
        for(GeoModel model: latlngs){
            LatLng coordinates = new LatLng(model.getLatitude(),model.getLongitude());
            myAreaList.add(coordinates);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

        if(mMap != null){
            mMap.clear();
            Double[] location = GPS_Service.getCurrentLocation(MapActivity.this);
            latitutude = location[0];
            longitude = location[1];
            addMarker(new LatLng(latitutude,longitude));
        }

    }

    @Override
    public void onLoadLocationFailure(String message) {

    }
}

package com.example.localnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.localnews.Geofence.GPS_Service;
import com.example.localnews.Geofence.PlaceListAdapter;
import com.google.android.gms.location.Geofence;


import java.util.List;

public class GeoFence extends AppCompatActivity  {


    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;

    private static List<Geofence> mGeofenceList;

    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);

        mRecyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaceListAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);


        Button btnAddGeofence = (Button)findViewById(R.id.btnAddGeofence);
        Button btnRemoveGeofence = (Button)findViewById(R.id.btnRemoveGeofence);

        btnAddGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double[] location = GPS_Service.getCurrentLocation(GeoFence.this);

                mAdapter.swapPlaces(mGeofenceList);
            }
        });

    }
}

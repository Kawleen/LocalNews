package com.example.localnews.Geofence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.localnews.R;
import com.google.android.gms.location.Geofence;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private Context mContext;
    private List<Geofence> mGeofenceList;

    public PlaceListAdapter(Context context, List<Geofence> geofenceList) {
        this.mContext = context;
        this.mGeofenceList = geofenceList;
    }

    @NonNull
    @Override
    public PlaceListAdapter.PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_place_card, viewGroup, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceListAdapter.PlaceViewHolder placeViewHolder, int position) {
        String placeName = mGeofenceList.get(position).toString();
        String placeAddress = mGeofenceList.get(position).getRequestId();
        placeViewHolder.nameTextView.setText(placeName);
        placeViewHolder.addressTextView.setText(placeAddress);
    }
    public void swapPlaces(List<Geofence> geofenceList){
        mGeofenceList = geofenceList;
        if (mGeofenceList != null) {

            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if(mGeofenceList==null) return 0;
        return mGeofenceList.size();
    }
    class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView addressTextView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            addressTextView = (TextView) itemView.findViewById(R.id.address_text_view);
        }

    }
}

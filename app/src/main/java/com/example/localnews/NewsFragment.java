package com.example.localnews;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.localnews.Geofence.LocationList;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import com.example.localnews.Adapter.FeedAdapter;
import com.example.localnews.Common.HTTPDataHandler;
import com.example.localnews.Model.RSSObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.localnews.Geofence.GPS_Service.getCurrentLocation;

public class NewsFragment extends Fragment {

    RecyclerView recyclerView;
    RSSObject rssObject;

    DatabaseReference databaseReference;

    private final String RSS_to_Json_API = " https://api.rss2json.com/v1/api.json?rss_url=";
    private String RSS_link;
    private String Previous_Rss_Link;
    private String countryName;
    private List<String> rsslist;

    private String result;


    private double currentLat;
    private double currentLong;
    private String currentCountry;
    LocationList locationList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        rsslist = new ArrayList<>();

        Previous_Rss_Link = "";

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        runHandler();
        return view;
    }

    private void runHandler(){

        currentLat = -34;
        currentLong = 151;
        currentCountry = "";

        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                Double[] location = getCurrentLocation(getContext());
                Double latitutude = location[0];
                Double longitude = location[1];

                Bundle bundle = getArguments();
                if (bundle != null) {
                    String url1 = bundle.getString("link");
                }else {

                }

                if(latitutude != currentLat || longitude != currentLong) {
                    currentLat = latitutude;
                    currentLong = longitude;
                    String country = getCountryName(getContext());
                    if(currentCountry.equalsIgnoreCase(country)){
                        return;
                    }else{
                        currentCountry = country;
                        locationList = new LocationList(country);
                        String url = locationList.getRssUrl();
                        if(url != ""){
                            loadRSS(url,RSS_to_Json_API);
                        }
                    }
                }
                ha.postDelayed(this, 1000);
            }
        }, 1000);}

    public static String getCountryName(Context c){
        String countryName = "";
        try {
            Double[] location = getCurrentLocation(c.getApplicationContext());
            Geocoder gcd = new Geocoder(c.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(location[0], location[1], 1);
            if (addresses.size() > 0) {
                countryName = addresses.get(0).getCountryName();
            }
        }catch (Exception e){
        }
        return countryName;
    }

    private void loadRSS(String RSS_link, String RSS_to_Json_API){
        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        new MyAsyncTask(getActivity(), recyclerView).execute(url_get_data.toString());
    }

    class MyAsyncTask extends AsyncTask<String, String, String> {
        RecyclerView mRecyclerView;
        Activity mContex;
        ProgressDialog mDialog = new ProgressDialog(getActivity());

            public  MyAsyncTask(Activity contex,RecyclerView rview)
        {
            this.mRecyclerView=rview;
            this.mContex=contex;
        }
        protected String doInBackground(String... params) {
            String result;
            HTTPDataHandler http = new HTTPDataHandler();
            result = http.GetHTTPData(params[0]);
            return  result;

        }
        @Override
        protected void onPreExecute() {
            mDialog.setMessage("Please wait...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            mDialog.dismiss();
            rssObject = new Gson().fromJson(s,RSSObject.class);
            FeedAdapter adapter = new FeedAdapter(rssObject,getActivity().getBaseContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
package com.example.localnews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.localnews.Youtube.ChannelModel;
import com.example.localnews.Youtube.ChannelModelUser;
import com.example.localnews.Youtube.YoutubeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsChannels extends Fragment {

    EditText youtubeUrl;
    EditText countryName;
    Button addChannel;
    RecyclerView rc_channelList;

    DatabaseReference youtubeDbRefAdmin;
    DatabaseReference youtubeDbRefUser;
    List<ChannelModel> channelModelList;

    FirebaseUser currentFirebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_channel,container,false);

        youtubeDbRefAdmin = FirebaseDatabase.getInstance().getReference("YoutubeLinksAdmin");
        youtubeDbRefUser = FirebaseDatabase.getInstance().getReference("YoutubeLinksUser");


        addChannel = (Button) view.findViewById(R.id.btnAddUrl);
        youtubeUrl = (EditText) view.findViewById(R.id.edit_youtubeUrl);
        countryName = (EditText) view.findViewById(R.id.edit_countryName);

        rc_channelList = (RecyclerView) view.findViewById(R.id.rv_channels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);
        rc_channelList.setLayoutManager(linearLayoutManager);

        channelModelList = new ArrayList<>();

        addChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChannel();
            }
        });

        return view;
    }

    protected void adminOnStart(){
        youtubeDbRefAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                channelModelList.clear();
                for(DataSnapshot linkSnapshot : dataSnapshot.getChildren()){
                    ChannelModel model = linkSnapshot.getValue(ChannelModel.class);
                    channelModelList.add(model);
                }
                YoutubeAdapter adapter = new YoutubeAdapter(channelModelList,getActivity().getBaseContext());
                rc_channelList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void userOnStart(String uid){
        Query ref = youtubeDbRefUser.orderByChild("country").equalTo(uid);
        youtubeDbRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot linkSnapshot : dataSnapshot.getChildren()){
                    ChannelModelUser model = linkSnapshot.getValue(ChannelModelUser.class);
                    channelModelList.add(new ChannelModel(model.getUrlID(),model.getUrl(),model.getCountry()));
                }

                YoutubeAdapter adapter = new YoutubeAdapter(channelModelList,getActivity().getBaseContext());
                rc_channelList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adminOnStart();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        userOnStart(uid);

    }

    private void AddChannel(){
        String url = youtubeUrl.getText().toString().trim();
        String country = countryName.getText().toString().trim();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();

        if(!TextUtils.isEmpty(url) || !TextUtils.isEmpty(country)){
            String id =  youtubeDbRefUser.push().getKey();

            ChannelModelUser link =  new ChannelModelUser(id,url,country,uid);
            youtubeDbRefUser.child(id).setValue(link);
            Toast.makeText(getContext(),"Link Added.",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getContext(),"Add Channel URL and Country Name.",Toast.LENGTH_SHORT).show();
        }
    }

}

package com.example.localnews;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.localnews.Adapter.RSSAdapter;
import com.example.localnews.Model.RSSModel;
import com.example.localnews.Model.RSSModelAdmin;
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

public class AddNewRSS extends AppCompatActivity {

    EditText rssUrl;
    EditText countryName;
    Button addRSS;
    RecyclerView rv_rss;

    DatabaseReference databaseReferenceAdmin;
    DatabaseReference databaseReferenceUser;
    List<RSSModel> rssList;
    List<RSSModelAdmin> rssAdminList;

    FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_rss);

        databaseReferenceAdmin = FirebaseDatabase.getInstance().getReference("RssAdminLinks");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("RssUserLinks");

        addRSS = (Button) findViewById(R.id.btnAddRSS);
        rssUrl = (EditText) findViewById(R.id.edit_rssURL);
        countryName = (EditText) findViewById(R.id.edit_RSSCountryName);

        rv_rss = (RecyclerView) findViewById(R.id.rv_rss);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        rv_rss.setLayoutManager(linearLayoutManager);

        rssAdminList = new ArrayList<>();
        addRSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRSS();
            }
        });
    }

    protected void AdminOnStart(){
        databaseReferenceAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rssAdminList.clear();

                for(DataSnapshot linkSnapshot : dataSnapshot.getChildren()){
                    RSSModelAdmin model = linkSnapshot.getValue(RSSModelAdmin.class);
                    rssAdminList.add(model);
                }

                RSSAdapter adapter = new RSSAdapter(rssAdminList,getApplicationContext());
                rv_rss.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void UserOnStart(String uid){
        Query ref = databaseReferenceUser.orderByChild("country").equalTo(uid);
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot linkSnapshot : dataSnapshot.getChildren()){
                    RSSModel model = linkSnapshot.getValue(RSSModel.class);
                    rssAdminList.add(new RSSModelAdmin(model.getUrlID(),model.getUrl(),model.getCountry()));
                }

                RSSAdapter adapter = new RSSAdapter(rssAdminList,getApplicationContext());
                rv_rss.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AdminOnStart();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        UserOnStart(uid);
    }

    private void AddRSS(){
        String url = rssUrl.getText().toString().trim();
        String country = countryName.getText().toString().trim();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();

        if(!TextUtils.isEmpty(url) || !TextUtils.isEmpty(country)){
            String id =  databaseReferenceUser.push().getKey();

            RSSModel link =  new RSSModel(id,url,country,uid);
            databaseReferenceUser.child(id).setValue(link);
            Toast.makeText(getBaseContext(),"Link Added.",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getBaseContext(),"Add RSS URL and Country Name.",Toast.LENGTH_SHORT).show();
        }
    }
}

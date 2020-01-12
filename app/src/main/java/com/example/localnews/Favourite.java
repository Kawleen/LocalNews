package com.example.localnews;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.localnews.Adapter.FeedAdapter;
import com.example.localnews.Model.Item;
import com.example.localnews.Model.RSSObject;
import com.example.localnews.SqlLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper mysqlDb;
    Item item;
    RSSObject rssObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mysqlDb = new DatabaseHelper(this);

        rssObject = object(mysqlDb);

        FeedAdapter adapter = new FeedAdapter(rssObject,getBaseContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public RSSObject object(DatabaseHelper mysqlDb) {
        Cursor result = mysqlDb.getAllData();
        List<Item> itemList = new ArrayList<>();
        if(result.getCount() == 0){
            // Empty Activity
        }
        while (result.moveToNext()) {
            final String link = result.getString(1);
            final String pubDate = result.getString(2);
            final String content = result.getString(3);
            final String title = result.getString(4);
            final String checkBox = result.getString(5);

            List<String> categories = new ArrayList<>();
            item = new Item(title,pubDate,link,"","","","",content,"", categories,checkBox);
            itemList.add(item);
        }
        rssObject = new RSSObject("",itemList);
        return rssObject;
    }
}
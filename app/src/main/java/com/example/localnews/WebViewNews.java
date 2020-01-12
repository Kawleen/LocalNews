package com.example.localnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.localnews.SqlLite.DatabaseHelper;

import java.util.Locale;

public class WebViewNews extends AppCompatActivity {

    private WebView mWebView;
    private CheckBox checkBoxFav;

    DatabaseHelper mysqlDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_news);

        mysqlDb = new DatabaseHelper(this);

        final Intent intent = getIntent();
        final String link = intent.getStringExtra("link");
        final String pubDate = intent.getStringExtra("pubDate");
        final String content = intent.getStringExtra("content");
        final String title = intent.getStringExtra("title");
        final String checkBox = intent.getStringExtra("checkBox");

        mWebView = (WebView) findViewById(R.id.RssWebView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString(String.valueOf(Locale.UK));
        mWebView.loadUrl(link);
        mWebView.setWebViewClient(new myWebViewClient());

        checkBoxFav = (CheckBox) findViewById(R.id.checkBoxFav);
        if(checkBox == "checked"){
            checkBoxFav.post(new Runnable() {
                @Override
                public void run() {
                    checkBoxFav.setChecked(true);
                }
            });
        }else{
        }
        checkBoxFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxFav.isChecked()){
                    boolean isInserted = mysqlDb.insertData(link,title,pubDate,content,"checked");
                    if(isInserted == true){
                        Toast.makeText(getApplicationContext(),"Added to Favourite",Toast.LENGTH_LONG).show();
                    }
                }else {
                    mysqlDb.deleteData(link);
                    Toast.makeText(getApplicationContext(),"Removed from Favourite",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

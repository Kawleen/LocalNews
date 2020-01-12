package com.example.localnews.Model;

public class RSSModel{
    String urlID;
    String url;
    String country;
    String userID;

    public RSSModel(){}

    public RSSModel(String urlID, String url, String country, String userID) {
        this.urlID = urlID;
        this.url = url;
        this.country = country;
        this.userID = userID;
    }

    public String getUrlID() {
        return urlID;
    }

    public String getUrl() {
        return url;
    }

    public String getCountry() {
        return country;
    }

    public String getUserID() {
        return userID;
    }
}

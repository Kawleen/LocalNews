package com.example.localnews.Youtube;

public class ChannelModelUser {
    String urlID;
    String url;
    String country;
    String userID;

    public ChannelModelUser(){}

    public ChannelModelUser(String urlID, String url, String country, String userID) {
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

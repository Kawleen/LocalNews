package com.example.localnews.Youtube;

public class ChannelModel {
    String urlID;
    String url;
    String country;

    public ChannelModel(){}

    public ChannelModel(String urlID, String url, String country) {
        this.urlID = urlID;
        this.url = url;
        this.country = country;
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
}

package com.example.localnews.Model;

public class RSSModelAdmin {
    String urlID;
    String url;
    String country;

    public RSSModelAdmin(){}

    public RSSModelAdmin(String urlID, String url, String country) {
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

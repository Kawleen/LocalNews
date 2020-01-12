package com.example.localnews.Geofence;

public class LocationList {

    private String mcountry;

    public LocationList(String country){
        this.mcountry = country;
    }

    public String getRssUrl(){
        String[][] urlList = {
                {"Germany","https://www.bild.de/rssfeeds/vw-alles/vw-alles-26970192,dzbildplus=false,sort=1,teaserbildmobil=false,view=rss2,wtmc=ob.feed.bild.xml"},
                {"Spain","http://ep00.epimg.net/rss/elpais/portada.xml"},
                {"France","https://www.lefigaro.fr/rss/figaro_actualites.xml"},
                {"Italy","http://xml2.corriereobjects.it/rss/homepage.xml"},
                {"Sweden","http://www.aftonbladet.se/rss.xml"},
                {"Neatherland","https://www.ad.nl/home/rss.xml"}
        };

        for(int i = 0; i < urlList.length;i++){
            if(mcountry.equals(urlList[i][0])){
                return urlList[i][1];
            }
        }
        return "";
    }
}

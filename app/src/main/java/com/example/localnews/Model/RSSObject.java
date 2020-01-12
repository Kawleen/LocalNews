package com.example.localnews.Model;

import java.util.List;

public class RSSObject
{
    public String status;
    public List<Item> items;

    public RSSObject(String status, List<Item> items) {
        this.status = status;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
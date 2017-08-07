package com.example.aburgess11.foodmood.models;

/**
 * Created by aburgess11 on 8/4/17.
 */

public class Friend {

    private String id;
    private String name ;
    private String url;

    public Friend(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

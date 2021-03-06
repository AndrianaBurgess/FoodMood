package com.example.aburgess11.foodmood;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aburgess11 on 7/12/17.
 */


public class SwipeProfile {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("location")
    @Expose
    private String location;


    public String getIngredients() {
        return ingredients;
    }

    public String getTimeofday() {
        return timeofday;
    }

    @SerializedName("ingredients")
    @Expose
    private String ingredients;

    @SerializedName("timeofday")
    @Expose
    private String timeofday;

    public String getAge() {
        return age;
    }

    @SerializedName("age")
    @Expose
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}


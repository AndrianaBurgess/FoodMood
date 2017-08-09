package com.example.aburgess11.foodmood.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by snhoward on 8/4/17.
 */

public class User {

    public String userId;
    public Map<String, Restaurant> restaurants;
//    public Map<String, Object> foodItems;
    public ArrayList<Object> foodItems;
    public List<Group> groups;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, List<Group> groups, Map<String, Restaurant> restaurants, ArrayList<Object> foodItems) {
        this.userId = userId;
        this.restaurants = restaurants;
        this.foodItems = foodItems;
        this.groups = groups;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Restaurant> getRestaurants() {
        return restaurants;
    }

    public ArrayList<Object> getFoodItems() {
        return foodItems;
    }

    public List<Group> getGroups() {
        return groups;
    }
}
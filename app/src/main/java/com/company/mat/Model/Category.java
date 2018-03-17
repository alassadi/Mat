package com.company.mat.Model;

import java.util.ArrayList;

/**
 * Created by Abood on 3/13/2018.
 */

public class Category {
    private String Name;
    private String Image;

    private ArrayList<String> RestaurantId;

    public Category() {
    }

    public Category(String name, String image) {
        this.Name = name;
        this.Image = image;
        RestaurantId = new ArrayList<>();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public void addRestaurant(String restaurant) {
        if (RestaurantId == null) {
            RestaurantId = new ArrayList<>();
        }
        RestaurantId.add(restaurant);
    }

    public ArrayList<String> getRestaurants() {
        return RestaurantId;
    }

    public void setRestaurantId(ArrayList<String> list) {
        RestaurantId = list;
    }
}

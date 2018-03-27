package com.company.mat.Model;

/**
 * Created by Abood on 3/17/2018.
 */

public class RestaurantModel {

    private String Name;
    private String Image;

    public RestaurantModel() {
    }

    public RestaurantModel(String name, String image) {
        this.Name = name;
        this.Image = image;
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
}
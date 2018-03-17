package com.company.mat.Model;

/**
 * Created by Abood on 3/13/2018.
 */

public class Category {
    private String Name;
    private String Image;
    private String RestaurantId;

    public Category() {

    }


    public Category(String name, String image, String restaurantId) {
        this.Name = name;
        this.Image = image;
        RestaurantId = restaurantId;
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


    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }
}

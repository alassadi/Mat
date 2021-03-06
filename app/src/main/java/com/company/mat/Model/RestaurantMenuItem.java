package com.company.mat.Model;

import java.io.Serializable;

/**
 * Created by ivana on 3/14/2018.
 */

public class RestaurantMenuItem implements Serializable {

    private String name, price, description;

    public RestaurantMenuItem() {
    }

    public RestaurantMenuItem(String name, String price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

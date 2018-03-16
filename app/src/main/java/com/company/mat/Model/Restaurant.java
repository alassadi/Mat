package com.company.mat.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivana on 3/10/2018.
 */

public class Restaurant implements Serializable {
    private String name;
    private String description;
    private String imageURL;
    private Address address;
    private RestaurantMenu menu;



    public Restaurant() {
        name = "";
        description = "";
    }

    public Restaurant(String name, Address address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
        imageURL = " ";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setMenu(RestaurantMenu menu) {
        this.menu = menu;
    }

    public RestaurantMenu getMenu() {
        return menu;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("address", address);
        map.put("image", imageURL);
        map.put("menu", menu.getMenu());
        return map;
    }

}

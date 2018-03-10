package com.company.mat.Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivana on 3/10/2018.
 */

public class Restaurant implements Serializable {
    private String name;
    private String description;
    private String address;
    //TODO add images


    public Restaurant() {
    }

    public Restaurant(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("address", address);
//        map.put("image",image);
        return map;
    }

}

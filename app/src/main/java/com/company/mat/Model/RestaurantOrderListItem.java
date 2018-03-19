package com.company.mat.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by ivana on 3/18/2018.
 *
 */

public class RestaurantOrderListItem implements Serializable {

    private String id, comments, time, price, pNumber, name;
    private String address;
    // items then amount
    private HashMap<String, String> items;

    public RestaurantOrderListItem() {
    }

    public RestaurantOrderListItem(String comments, String address, HashMap<String, String> items, String pNumber, String name, String price) {
        this.comments = comments;
        this.address = address;
        this.items = items;
        this.pNumber = pNumber;
        this.name = name;
        this.price = price;
        time = "";
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashMap<String, String> getItems() {
        return items;
    }

    public void setItems(HashMap<String, String> items) {
        this.items = items;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comments", comments);
        hashMap.put("time", time);
        hashMap.put("address", address);
        hashMap.put("items", items);
        hashMap.put("price", price);
        return hashMap;
    }
}

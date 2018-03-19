package com.company.mat.Model;

/**
 * Created by Abood on 3/18/2018.
 */

public class FoodOrder {
    private String itemId;
    private String itemName;
    private String quantity;
    private String price;

    public FoodOrder() {
    }

    public FoodOrder(String itemId, String itemName, String quantity, String price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
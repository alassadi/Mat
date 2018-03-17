package com.company.mat.Model;

/**
 * Created by Abood on 3/15/2018.
 */

public class Food {
    private String Name;
    private String Image;
    private String Description;
    private String Price;
    private String MenuId;

    public Food() {
    }

    public Food(String name, String image, String description, String price, String menuId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}

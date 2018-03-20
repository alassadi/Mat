package com.company.mat.Model;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abood on 3/13/2018.
 */

public class Category {
    private String Name;
    private String Image;

    private ArrayList<Food> foods;

    public Category() {
    }

    public Category(String name, String image) {
        this.Name = name;
        this.Image = image;
        foods = new ArrayList<>();
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

    public void addFood(Food food) {
        if (foods == null) {
            foods = new ArrayList<>();

        }
        foods.add(food);
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> list) {
        foods = list;
    }

    public Food getFood(int index) {
        return foods.get(index);
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", Name);
        map.put("image", Image);
        map.put("foods", foods);
        return map;
    }
}

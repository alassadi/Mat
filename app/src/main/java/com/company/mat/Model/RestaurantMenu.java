package com.company.mat.Model;

/**
 * Created by ivana on 3/12/2018.
 */

import android.util.Log;

import com.company.mat.ListItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantMenu implements Serializable {

    private HashMap<String, List<ListItem<String>>> menu;

    public RestaurantMenu() {
        menu = new HashMap<>();
        if (!menu.containsKey("Add Category")) {
            menu.put("Add Category", new ArrayList<ListItem<String>>());


        }
    }

    public void addCategory(String categoryName) {
        List<ListItem<String>> item = new ArrayList<>();
        item.add(new ListItem<>("add Item"));
        menu.put(categoryName, item);
        Log.w("RestaurantMenu", "addCategory() called");
    }

    public void addItemToCategory(String categoryName, String itemName) {
        menu.get(categoryName).add(new ListItem<>(itemName));

    }

    public HashMap<String, List<ListItem<String>>> getMenu() {
        return menu;
    }

    public void removeItem(String categoryName, String listItem) {
        menu.get(categoryName).remove(listItem);
    }

    public void removeCategory(String categoryName) {
        menu.remove(categoryName);
    }
}

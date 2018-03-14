package com.company.mat.Model;

/**
 * Created by ivana on 3/12/2018.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantMenu implements Serializable {

    private HashMap<String, List<MenuItem>> menu;

    public RestaurantMenu() {
        menu = new HashMap<>();
        if (!menu.containsKey("Add Category")) {
            menu.put("Add Category", new ArrayList<MenuItem>());

        }
    }

    public void addCategory(String categoryName) {
        List<MenuItem> item = new ArrayList<>();
        item.add(new MenuItem("add Item", "", ""));
        menu.put(categoryName, item);
    }

    public void addItemToCategory(String categoryName, String itemName) {
        menu.get(categoryName).add(new MenuItem(itemName, "", ""));

    }

    public HashMap<String, List<MenuItem>> getMenu() {
        return menu;
    }

    public void removeItem(String categoryName, String listItem) {
        menu.get(categoryName).remove(listItem);
    }

    public void removeCategory(String categoryName) {
        menu.remove(categoryName);
    }

    public MenuItem getItemInCategory(String parent, int item) {
        return menu.get(parent).get(item);
    }

    public void replaceItemInCategory(String categoryName, int itemNo, MenuItem newObject) {
        menu.get(categoryName).remove(itemNo);
        menu.get(categoryName).add(itemNo, newObject);

    }

}

package com.company.mat.Model;

/**
 * Created by ivana on 3/12/2018.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantMenu implements Serializable {

    private HashMap<String, List<RestaurantMenuItem>> menu;

    public RestaurantMenu() {
        menu = new HashMap<>();
        if (!menu.containsKey("Add Category")) {
            menu.put("Add Category", new ArrayList<RestaurantMenuItem>());

        }
    }

    public void addCategory(String categoryName) {
        List<RestaurantMenuItem> item = new ArrayList<>();
        item.add(new RestaurantMenuItem("add Item", "", ""));
        menu.put(categoryName, item);
    }

    public void addItemToCategory(String categoryName, String itemName) {
        menu.get(categoryName).add(new RestaurantMenuItem(itemName, "", ""));

    }

    public HashMap<String, List<RestaurantMenuItem>> getMenu() {
        return menu;
    }

    public void removeItem(String categoryName, RestaurantMenuItem listItem) {
        menu.get(categoryName).remove(listItem);
    }

    public void removeCategory(String categoryName) {
        menu.remove(categoryName);
    }

    public RestaurantMenuItem getItemInCategory(String parent, int item) {
        return menu.get(parent).get(item);
    }

    public void replaceItemInCategory(String categoryName, int itemNo, RestaurantMenuItem newObject) {
        menu.get(categoryName).remove(itemNo);
        menu.get(categoryName).add(itemNo, newObject);
    }

    public void renameCategory(String oldName, String newName) {
        menu.put(newName, menu.remove(oldName));
    }

}

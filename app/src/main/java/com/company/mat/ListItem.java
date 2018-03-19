package com.company.mat;

import java.util.ArrayList;

/**
 * Created by ivana on 3/10/2018.
 *
 * Requiured for view adapter, in order to avoid using 2 dimensional arrays.
 */

public class ListItem<T> {
    private ArrayList<T> elements;

    public ListItem(T entry) {
        elements = new ArrayList<>();
        elements.add(entry);
    }

    public ListItem() {
    }

    public T getEntry(int index) {
        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        } else {
            return null;
        }
    }

    public void replaceEntry(T entry, int index) {
        elements.set(index, entry);
    }

    public void add(T entry) {
        elements.add(entry);
    }

    public int size() {
        return elements.size();
    }


}

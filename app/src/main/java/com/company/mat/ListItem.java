package com.company.mat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ivana on 3/10/2018.
 *
 * Requiured for view adapter, in order to avoid using 2 dimensional arrays.
 */

public class ListItem<T> implements Serializable {
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
        if (index >= elements.size()) {
            elements.add(entry);
        }
        if (index < 0) {
            return;
        }
        elements.set(index, entry);
    }

    public void remove(T object) {
        elements.remove(object);
    }

    public void add(T entry) {
        elements.add(entry);
    }

    public int size() {
        return elements.size();
    }


}

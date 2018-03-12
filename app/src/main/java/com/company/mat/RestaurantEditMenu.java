package com.company.mat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.company.mat.Model.RestaurantMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantEditMenu extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<ListItem<String>>> expandableListDetail;

    private RestaurantMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_menu);


        if (getIntent().getSerializableExtra("restaurant") != null) {
            menu = (RestaurantMenu) getIntent().getSerializableExtra("menu");
        } else {
            Toast.makeText(this, "menu is null", Toast.LENGTH_SHORT).show();
            menu = new RestaurantMenu();

        }

        updateList();

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                String selection = expandableListTitle.get(groupPosition);
                if (selection.equalsIgnoreCase("add category")) {
                    menu.addCategory("new category");
                    updateList();
                }
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selection = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getEntry(0);
                //TODO allow editing
                if (selection.equalsIgnoreCase("add item")) {
                    menu.addItemToCategory(expandableListTitle.get(groupPosition), "new Item");
                    updateList();
                    Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                    expandableListView.expandGroup(groupPosition);
                }
                return false;
            }
        });
    }

    private void updateList() {
        expandableListView = findViewById(R.id.restaurantEditMenu);
        expandableListDetail = menu.getMenu();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }

}
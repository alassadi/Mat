package com.company.mat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.company.mat.Model.MenuItem;
import com.company.mat.Model.RestaurantMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantEditMenu extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<MenuItem>> expandableListDetail;

    private DatabaseReference dbref;


    private RestaurantMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_menu);


        if (getIntent().getSerializableExtra("menu") != null) {
            menu = (RestaurantMenu) getIntent().getSerializableExtra("menu");
            menu.addCategory("Add Category");
        } else {
            Toast.makeText(this, "menu is null", Toast.LENGTH_SHORT).show();
            menu = new RestaurantMenu();
        }

        updateList();

        dbref = FirebaseDatabase.getInstance().getReference()
                .child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu");

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

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menu = dataSnapshot.getValue(RestaurantMenu.class);
                if (menu != null) {
                    menu.addCategory("Add Category");
                    updateList();
                } else {
                    if (getIntent().getSerializableExtra("menu") != null) {
                        menu = (RestaurantMenu) getIntent().getSerializableExtra("menu");
                        menu.addCategory("Add Category");

                    } else {
                        Toast.makeText(expandableListView.getContext(), "menu is null", Toast.LENGTH_SHORT).show();
                        menu = new RestaurantMenu();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // log a message
                Log.w(" ", "loadPost:onCancelled", databaseError.toException());
            }
        };
        dbref.addValueEventListener(eventListener);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selection = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getName();


                if (selection.equalsIgnoreCase("add item")) {
                    menu.addItemToCategory(expandableListTitle.get(groupPosition), "new Item");
                    updateList();
                    //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                    expandableListView.expandGroup(groupPosition);
                } else {
                    Intent intent = new Intent(parent.getContext(), MenuItemEdit.class);
                    intent.putExtra("menu", menu);
                    intent.putExtra("parent", expandableListTitle.get(groupPosition));
                    intent.putExtra("item", selection);
                    intent.putExtra("itemNo", childPosition);
                    intent.putExtra("price", expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getPrice());
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        expandableListView = findViewById(R.id.restaurantEditMenu);
        expandableListDetail = menu.getMenu();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }

    //TODO allow deletion of a list item and category

}
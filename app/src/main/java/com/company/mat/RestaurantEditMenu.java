package com.company.mat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.company.mat.Model.RestaurantMenuItem;
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

public class RestaurantEditMenu extends AppCompatActivity implements CustomExpandableListAdapter.LongPress {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<RestaurantMenuItem>> expandableListDetail;

    private DatabaseReference dbref;
    private String currentSelection = null;



    private RestaurantMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_menu);
        Toolbar toolbar = findViewById(R.id.restaurantEditMenuToolbar);
        setSupportActionBar(toolbar);



        if (getIntent().getSerializableExtra("menu") != null) {
            menu = (RestaurantMenu) getIntent().getSerializableExtra("menu");
            menu.addCategory("Add Category");
        } else {
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
                    Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restaurant_edit_toolbar, menu);
        return true;
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


    @Override
    public void longPressedChild(String category, String groupPosition, String childPosition) {
        currentSelection = "E:" + category + ":" + groupPosition + ":" + childPosition;
    }

    @Override
    public void longPressedCategory(String category) {
        currentSelection = "C:" + category;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (currentSelection == null) {
            return true;
        }
        String[] arr = currentSelection.split(":");
        if (arr[1].equalsIgnoreCase("Add Category") || arr[1].equalsIgnoreCase("Add item")) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.editMenu:
                if (arr[0].equals("E")) {
                    Intent intent = new Intent(this, MenuItemEdit.class);
                    intent.putExtra("menu", menu);
                    intent.putExtra("parent", expandableListTitle.get(Integer.parseInt(arr[2])));
                    intent.putExtra("item", arr[1]);
                    intent.putExtra("itemNo", Integer.parseInt(arr[3]));
                    intent.putExtra("price", expandableListDetail.get(expandableListTitle.get(Integer.parseInt(arr[2]))).get(Integer.parseInt(arr[3])).getPrice());
                    startActivity(intent);
                } else if (arr[0].equals("C")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);

                    alert.setTitle("Rename Category");
                    alert.setMessage("Enter new name for the category.");

                    final EditText input = new EditText(this);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            menu.renameCategory(currentSelection.split(":")[1], input.getText().toString());
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu");
                            dbref.setValue(menu);
                            updateList();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();
                }
                return true;
            case R.id.deleteEntry:
                if (arr[1].equalsIgnoreCase("Add Category") || arr[1].equalsIgnoreCase("Add item")) {
                    return true;
                }
                if (arr[0].equals("E")) {
                    Log.w("del", arr[1]);
                    String category = expandableListTitle.get(Integer.parseInt(arr[2]));
                    menu.removeItem(category, menu.getItemInCategory(category, Integer.parseInt(arr[3])));
                } else if (arr[0].equals("C")) {
                    Log.w("del", arr[1]);
                    menu.removeCategory(arr[1]);
                }
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu");
                dbref.setValue(menu);
                updateList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
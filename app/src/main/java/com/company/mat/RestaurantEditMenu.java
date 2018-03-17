package com.company.mat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.company.mat.Model.Category;
import com.company.mat.Model.Food;
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

    private RestaurantMenu menu;

    private ArrayList<Category> categories;
    private ArrayList<Food> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_menu);
        categories = new ArrayList<>();
        foods = new ArrayList<>();


        if (getIntent().getSerializableExtra("menu") != null) {
            menu = (RestaurantMenu) getIntent().getSerializableExtra("menu");
            menu.addCategory("Add Category");
        } else {
            menu = new RestaurantMenu();
        }

        updateList();

        dbref = FirebaseDatabase.getInstance().getReference()
                .child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu");


        // Adding all those listeners
        expandableListView.setOnGroupExpandListener(getGroupExpandListener());
        dbref.addValueEventListener(getMenuListener());
        expandableListView.setOnChildClickListener(getChildListener());

        // getting list of categories
        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            categories.add(ds.getValue(Category.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FireBase database says:", databaseError.getMessage());
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            foods.add(ds.getValue(Food.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FireBase database says:", databaseError.getMessage());
                    }
                });
            }
        }).start();
    }


    private ExpandableListView.OnGroupExpandListener getGroupExpandListener() {
        return new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                String selection = expandableListTitle.get(groupPosition);
                if (selection.equalsIgnoreCase("Add Category")) {
                    expandableListView.collapseGroup(groupPosition);
                    showDialog(selection, false);
                }
            }
        };
    }

    private ValueEventListener getMenuListener() {
        return new ValueEventListener() {
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
    }

    private ExpandableListView.OnChildClickListener getChildListener() {
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selection = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getName();


                if (selection.equalsIgnoreCase("add item")) {
                    showDialog(expandableListTitle.get(groupPosition), true);
                    updateList();
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
                return true;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        expandableListView = findViewById(R.id.restaurantEditMenu);
        menu.addCategory("Add Category");
        expandableListDetail = menu.getMenu();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
    }


    @Override
    public void longPressedCategory(String category) {
        showDialog(category, false);
        updateList();
    }

    @Override
    public void removeCategory(String category) {

        final String cat = category;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Remove Category");
        alert.setMessage("The category will be deleted.");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                menu.removeCategory(cat);
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

    private void showDialog(String category, final boolean child) {
        final ArrayList<String> displayValues = new ArrayList<>();
        final String cat = category;

        if (!child) {
            for (int i = 0; i < categories.size(); i++) {
                displayValues.add(categories.get(i).getName());
            }
        } else {

            for (int i = 0; i < foods.size(); i++) {
                if (foods.get(i).getMenuId().equalsIgnoreCase(category)) {
                    displayValues.add(foods.get(i).getName());
                }
            }
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (!child) {
            if (cat.equalsIgnoreCase("Add Category")) {
                alert.setTitle("Add Category");
                alert.setMessage("Choose a category.");
            } else {
                alert.setTitle("Rename Category");
                alert.setMessage("Choose new name for the category.");
            }
        } else {
            alert.setTitle("Add Food");
            alert.setMessage("Choose a name of a food.");
        }
        final NumberPicker numberPicker = new NumberPicker(expandableListView.getContext());
        alert.setView(numberPicker);


        String[] uselessArray = new String[displayValues.size()];
        for (int i = 0; i < uselessArray.length; i++) {
            uselessArray[i] = displayValues.get(i);
        }


        numberPicker.setDisplayedValues(uselessArray);
        Log.e("", displayValues.size() + "");
        if (!child) {
            numberPicker.setMaxValue(categories.size() - 1);
        } else {
            numberPicker.setMaxValue(displayValues.size() - 1);
        }
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!child) {
                    if (cat.equalsIgnoreCase("Add Category")) {
                        menu.addCategory(categories.get(numberPicker.getValue()).getName());
                    } else {
                        menu.renameCategory(cat, categories.get(numberPicker.getValue()).getName());
                    }
                } else {
                    menu.addItemToCategory(cat, displayValues.get(numberPicker.getValue()));
                }
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu");
                dbref.setValue(menu);

                Toast.makeText(expandableListView.getContext(), menu.getMenu().containsKey("Add Category") + "", Toast.LENGTH_SHORT).show();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }


}
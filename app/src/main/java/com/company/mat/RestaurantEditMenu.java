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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.company.mat.Model.Category;
import com.company.mat.Model.Food;
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
    private HashMap<String, ArrayList<Food>> expandableListDetail;

    private ArrayList<Category> categories = new ArrayList<>();
    private Food newFood = new Food("Add Item", "", "", "", "");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_menu);


        loadCategories();
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

    private ExpandableListView.OnChildClickListener getChildListener() {
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selection = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getName();
                if (selection.equalsIgnoreCase("add item")) {
                    showDialog(expandableListTitle.get(groupPosition), true);
                    expandableListView.expandGroup(groupPosition);
                } else {
                    Intent intent = new Intent(parent.getContext(), MenuItemEdit.class);
                    intent.putExtra("parent", expandableListTitle.get(groupPosition));
                    intent.putExtra("item", expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
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
    }

    private void updateList(ArrayList<Category> categories) {
        expandableListView = findViewById(R.id.restaurantEditMenu);
        HashMap<String, ArrayList<Food>> map = new HashMap<>();
        for (Category c : categories) {
            //c.addFood(new Food("add Item"," "," "," ",FirebaseAuth.getInstance().getCurrentUser().getUid()));
            map.put(c.getName(), c.getFoods());
        }
        if (!map.containsKey("Add Category")) {
            map.put("Add Category", new ArrayList<Food>());
        }
        expandableListDetail = map;
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(getGroupExpandListener());
        expandableListView.setOnChildClickListener(getChildListener());

    }


    @Override
    public void longPressedCategory(String category) {
        showDialog(category, false);
    }

    @Override
    public void removeCategory(final String category) {

        final String cat = category;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Remove Category");
        alert.setMessage("The category will be deleted.");


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                for (Category c : categories) {
                    Log.e("categories.size", categories.size() + " ");
                    if (c.getName().equalsIgnoreCase(category)) {
                        categories.remove(c);
                        break;
                    }
                }
                Log.e("categories.size", categories.size() + " ");

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("menu").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                HashMap<String, Object> map = new HashMap<>();
                for (Category c : categories) {
                    map.put(c.getName(), c);
                    c.getFoods().remove(newFood);
                }
                dbref.setValue(map);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void showDialog(final String category, final boolean child) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (!child) {
            if (category.equalsIgnoreCase("Add Category")) {
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


        LinearLayout layout = new LinearLayout(this);
        final EditText name = new EditText(this);
        final EditText image = new EditText(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        image.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setHint(R.string.name);
        image.setHint(R.string.image);
        layout.addView(name);
        layout.addView(image);
        alert.setView(layout);
        final String cat = category;
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (name.getText().length() < 2) {
                    return;
                }
                int category1 = -1;
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getName().equalsIgnoreCase(cat)) {
                        category1 = i;
                        break;
                    }
                }
                if (!child) {
                    if (cat.equalsIgnoreCase("Add Category")) {

                        categories.add(new Category(name.getText().toString(), image.getText().toString()));


                    } else {

                        if (category1 != -1) {
                            categories.get(category1).setName(name.getText().toString());
                            categories.get(category1).setImage(image.getText().toString());
                        }
                    }
                } else {
                    categories.get(category1).addFood(new Food(name.getText().toString(), image.getText().toString(), " ", " ", " "));

                }
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("menu").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                HashMap<String, Object> map = new HashMap<>();
                for (Category c : categories) {
                    categories.get(categories.indexOf(c)).getFoods().remove(newFood);
                    map.put(c.getName(), c);

                }
                dbref.setValue(map);

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }

    private void loadCategories() {
        FirebaseDatabase.getInstance().getReference()
                .child("menu").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Category c = ds.getValue(Category.class);
                    if (c != null) {
                        c.addFood(newFood);
                    }
                    categories.add(c);
                    Log.e("read", ds.toString());
                }
                updateList(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
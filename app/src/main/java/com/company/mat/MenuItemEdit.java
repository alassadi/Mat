package com.company.mat;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.company.mat.Model.Food;
import com.company.mat.Model.RestaurantMenuItem;
import com.company.mat.Model.RestaurantMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MenuItemEdit extends AppCompatActivity {

    private DatabaseReference dbref;
    private EditText name, description, price;

    private String parent, item;
    private Food menuItem;
    private ImageView image;
    private Context context;
    private int itemNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_edit);
        context = this;
        dbref = FirebaseDatabase.getInstance().getReference();

        Button btn = findViewById(R.id.MenuSaveInputButton);
        name = findViewById(R.id.editTextMenuName);
        description = findViewById(R.id.editTextMenuDescription);
        price = findViewById(R.id.editTextMenuPrice);
        image = findViewById(R.id.restaurantMenuFoodItem);


        Button delete = findViewById(R.id.MenuDeleteItemButton);


        if (getIntent().getSerializableExtra("item") != null) {
            menuItem = (Food) getIntent().getSerializableExtra("item");
            item = menuItem.getName();
        } else {
            item = "";
        }

        if (getIntent().getStringExtra("parent") != null) {
            parent = getIntent().getStringExtra("parent");
        } else {
            parent = "null";
        }

        name.setText(item);
        if (getIntent().getStringExtra("price") != null) {
            price.setText(getIntent().getStringExtra("price"));
        } else {
            price.setText("");
        }

        if (menuItem != null && menuItem.getDescription() != null) {
            description.setText(menuItem.getDescription());
        }
        if (menuItem == null) {
            Snackbar.make(name, "menuItem is null", Snackbar.LENGTH_SHORT).show();
        }
        itemNo = getIntent().getIntExtra("itemNo", 0);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.setName(name.getText().toString());
                menuItem.setPrice(price.getText().toString());
                menuItem.setDescription(description.getText().toString());
                dbref = FirebaseDatabase.getInstance().getReference().child("menu").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(parent).child("foods");
                dbref.child(String.valueOf(itemNo)).setValue(menuItem);

                onBackPressed();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Delete Item");
                alert.setMessage("This item will be deleted.");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dbref = FirebaseDatabase.getInstance().getReference().child("restaurants").child(
                                FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu").child(parent).child("foods");

                        onBackPressed();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();


            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(image.getContext());
                alert.setTitle("Food Picture");
                alert.setMessage("Enter URL for your picture.");
                final EditText editText = new EditText(image.getContext());
                alert.setView(editText);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setImage(editText.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("menu").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageURL").setValue(editText.getText().toString());

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });
    }

    public void setImage(String url) {
        try {
            Picasso.get().load(url).into(image);
            menuItem.setImage(url);
        } catch (Exception e) {
            Log.e("picasso", e.getMessage());
        }
    }
}

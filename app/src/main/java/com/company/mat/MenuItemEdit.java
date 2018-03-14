package com.company.mat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.company.mat.Model.MenuItem;
import com.company.mat.Model.RestaurantMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuItemEdit extends AppCompatActivity {

    private DatabaseReference dbref;
    private EditText name, description, price;

    private String parent, item;
    private MenuItem menuItem;
    private RestaurantMenu menu;
    private int itemNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_edit);

        dbref = FirebaseDatabase.getInstance().getReference();

        Button btn = findViewById(R.id.MenuSaveInputButton);
        name = findViewById(R.id.editTextMenuName);
        description = findViewById(R.id.editTextMenuDescription);
        price = findViewById(R.id.editTextMenuPrice);


        if (getIntent().getSerializableExtra("menu") != null) {
            menu = (RestaurantMenu) getIntent().getSerializableExtra("menu");
        } else {
            menu = new RestaurantMenu();
        }

        if (getIntent().getStringExtra("item") != null) {
            item = getIntent().getStringExtra("item");
        } else {
            item = "";
        }

        if (getIntent().getStringExtra("parent") != null) {
            parent = getIntent().getStringExtra("parent");
        } else {
            parent = "";
        }

        name.setText(item);
        if (getIntent().getStringExtra("price") != null) {
            price.setText(getIntent().getStringExtra("price"));
        } else {
            price.setText("");
        }

        itemNo = getIntent().getIntExtra("itemNo", 0);
        menuItem = menu.getItemInCategory(parent, itemNo);
        description.setText(menuItem.getDescription());


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.setName(name.getText().toString());
                menuItem.setPrice(price.getText().toString());
                menuItem.setDescription(description.getText().toString());
                menu.replaceItemInCategory(parent, itemNo, menuItem);
                menu.removeCategory("Add Category");
                dbref = FirebaseDatabase.getInstance().getReference().child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu");
                dbref.setValue(menu);

                onBackPressed();
            }
        });
    }
}

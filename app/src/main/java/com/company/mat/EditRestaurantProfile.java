package com.company.mat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.mat.Model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class EditRestaurantProfile extends AppCompatActivity {

    private DatabaseReference dbref;
    private EditText name, address, description;
    private Restaurant restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant_profile);

        dbref = FirebaseDatabase.getInstance().getReference();

        Button btn = findViewById(R.id.RestaurantSaveInputButton);
        name = findViewById(R.id.editTextRestaurantName);
        description = findViewById(R.id.editTextRestaurantDescription);

        if (getIntent().getSerializableExtra("restaurant") != null) {
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
            name.setText(restaurant != null ? restaurant.getName() : "name");
            description.setText(restaurant.getDescription());
        } else {
            Toast.makeText(this, "restaurant is null", Toast.LENGTH_SHORT).show();
        }




        //TODO add images
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref = FirebaseDatabase.getInstance().getReference().child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                restaurant.setDescription(description.getText().toString());
                restaurant.setName(name.getText().toString());
                dbref.setValue(restaurant);
                HashMap<String, Object> children = new HashMap<>();
                children.put("name", restaurant.getName());
                children.put("description", restaurant.getDescription());
                dbref.updateChildren(children);
                onBackPressed();
            }
        });
    }
}

package com.company.mat;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.company.mat.Model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditRestaurantProfile extends AppCompatActivity {

    private DatabaseReference dbref;
    private EditText name, address, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant_profile);

        dbref = FirebaseDatabase.getInstance().getReference();

        Button btn = findViewById(R.id.RestaurantSaveInputButton);
        name = findViewById(R.id.editTextRestaurantName);
        address = findViewById(R.id.editTextRestaurantAddress);
        description = findViewById(R.id.editTextRestaurantDescription);


        //TODO add images
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref = FirebaseDatabase.getInstance().getReference().child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Restaurant restaurant = new Restaurant(name.getText().toString(), address.getText().toString(), description.getText().toString());
                dbref.setValue(restaurant);

                onBackPressed();
            }
        });
    }
}

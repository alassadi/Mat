package com.company.mat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.mat.Model.Address;
import com.company.mat.Model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantEditAddress extends AppCompatActivity {

    private Restaurant restaurant;
    private EditText street, city, postCode, region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit_address);


        street = findViewById(R.id.editTextRestaurantAddressStreet);
        city = findViewById(R.id.editTextRestaurantAddressCity);
        postCode = findViewById(R.id.editTextRestaurantAddressPostCode);
        region = findViewById(R.id.editTextRestaurantAddressRegion);
        Button saveButton = findViewById(R.id.RestaurantSaveInputButton);

        if (getIntent().getSerializableExtra("restaurant") != null
                && ((Restaurant) getIntent().getSerializableExtra("restaurant")).getAddress() != null) {
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
            street.setText(restaurant.getAddress().getStreet());
            city.setText(restaurant.getAddress().getCity());
            region.setText(restaurant.getAddress().getRegion());
            postCode.setText(restaurant.getAddress().getPostCode());
        } else {
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
            restaurant.setAddress(new Address());
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurant.setAddress(new Address(street.getText().toString(),
                        city.getText().toString(), region.getText().toString(),
                        postCode.getText().toString()));

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("restaurants")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("address");
                dbref.setValue(restaurant.getAddress());
                onBackPressed();
            }
        });
    }
}

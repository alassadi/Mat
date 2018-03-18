package com.company.mat;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.company.mat.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    TextView foodName, foodDescription, foodPrice;
    ImageView foodImage;
    String foodId = "";
    CollapsingToolbarLayout collapsingToolbarLayout;
    ElegantNumberButton elegantNumberButton;
    FloatingActionButton floatingActionButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        food = firebaseDatabase.getReference("Food");

        elegantNumberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.buttonCart);
        foodName = (TextView) findViewById(R.id.food_name);
        foodDescription = (TextView) findViewById(R.id.food_description);
        foodPrice = (TextView) findViewById(R.id.food_price);
        foodImage = (ImageView) findViewById(R.id.img_food);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarStyle);

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodID");
        }
        //and here to load the info from firebase
        if (!foodId.isEmpty()) {
            getDetailFood(foodId);
        }

    }

    private void getDetailFood(String foodId) {
        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.getValue(Food.class);
                if (food != null) {
                    foodName.setText(food.getName());
                    Picasso.get().load(food.getImage()).into(foodImage);
                    collapsingToolbarLayout.setTitle(food.getName());
                    foodPrice.setText(food.getPrice());
                    foodDescription.setText(food.getDescription());
                } else {
                    Toast.makeText(getApplicationContext(), "Food is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

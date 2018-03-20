package com.company.mat;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.company.mat.Model.Food;
import com.company.mat.Model.FoodOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodDetail extends AppCompatActivity {
    TextView foodName, foodDescription, foodPrice;
    ImageView foodImage;
    String foodId = "";
    CollapsingToolbarLayout collapsingToolbarLayout;
    ElegantNumberButton elegantNumberButton;
    FloatingActionButton floatingActionButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference food;
    DatabaseReference foodOrder;
    FoodOrder order;
    private Food foodObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        food = firebaseDatabase.getReference("menu");
        foodOrder = firebaseDatabase.getReference("FoodOrders");
        elegantNumberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.buttonCart);
        foodName = (TextView) findViewById(R.id.food_name);
        foodDescription = (TextView) findViewById(R.id.food_description);
        foodPrice = (TextView) findViewById(R.id.food_price);
        foodImage = (ImageView) findViewById(R.id.img_food);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarStyle);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = new FoodOrder(getIntent().getStringExtra("FoodID").toString(), foodName.getText().toString(), elegantNumberButton.getNumber().toString(), foodPrice.getText().toString(), getIntent().getStringExtra("RestaurantId"));
                ////////// order id
                foodOrder.child(FirebaseAuth.getInstance().getUid()).child(UUID.randomUUID().toString()).setValue(order);
                Toast.makeText(getApplicationContext(), "Added to Cart.", Toast.LENGTH_SHORT).show();

            }
        });

        if (getIntent() != null) {
            //foodId = getIntent().getStringExtra("FoodID");
            foodObj = (Food) getIntent().getSerializableExtra("Food");
            if (food != null) {
                foodName.setText(foodObj.getName());
                Picasso.get().load(foodObj.getImage()).into(foodImage);
                collapsingToolbarLayout.setTitle(foodObj.getName());
                foodPrice.setText(foodObj.getPrice());
                foodDescription.setText(foodObj.getDescription());
            } else {
                Toast.makeText(getApplicationContext(), "Food is null", Toast.LENGTH_SHORT).show();
            }

        }
        //and here to load the info from firebase
        if (!foodId.isEmpty()) {

            //getDetailFood(foodId);
        }

    }

    private void getDetailFood(String foodId) {
        food.child("Food").child(foodId).addValueEventListener(new ValueEventListener() {
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

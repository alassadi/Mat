package com.company.mat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.company.mat.Interface.ItemClickListener;
import com.company.mat.Model.Food;
import com.company.mat.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference food;
    String categoryId = "";
    String restaurantId = "";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Load the menu
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        // get intent info
        if (intent.getExtras() != null) {
            categoryId = extras.getString("CategoryId");
            restaurantId = extras.getString("RestaurantId");
            food = firebaseDatabase.getReference("menu").child(restaurantId).child(categoryId);
        }
        if (!categoryId.isEmpty() && categoryId != null) {
            loadFoodList(categoryId);
        }
    }

    private void loadFoodList(String category) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, food.child("foods")) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                try {
                    Picasso.get().load(model.getImage()).into(viewHolder.food_image);
                } catch (Exception e) {
                    Log.e("Picasso", e.getMessage());
                }
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent detail = new Intent(FoodList.this, FoodDetail.class);
                        Bundle extras = new Bundle();
                        extras.putSerializable("Food", model);
                        extras.putString("FoodID", adapter.getRef(position).getKey());
                        extras.putString("RestaurantId",getIntent().getStringExtra("RestaurantId"));
                        detail.putExtras(extras);
                        startActivity(detail);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}

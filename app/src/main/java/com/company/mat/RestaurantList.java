package com.company.mat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.company.mat.Interface.ItemClickListener;
import com.company.mat.Model.RestaurantModel;
import com.company.mat.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RestaurantList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference restaurantList;
    TextView fullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<RestaurantModel, RestaurantViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurants");
        setSupportActionBar(toolbar);
        */
        // firebase
        database = FirebaseDatabase.getInstance();
        restaurantList = database.getReference("RestaurantList");


        // set a name for the user // not working yet!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        //fullName = (TextView)headerView.findViewById(R.id.fullName);
        //fullName.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());

        // Load the menu
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_restaurant);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadRestaurants();

    }

    private void loadRestaurants() {
        adapter = new FirebaseRecyclerAdapter<RestaurantModel, RestaurantViewHolder>(RestaurantModel.class, R.layout.restaurant_item, RestaurantViewHolder.class, restaurantList) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder, RestaurantModel model, int position) {
                viewHolder.textRestaurantName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);
                final RestaurantModel clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // send category id to the food food list activity
                        Intent categoryIntent = new Intent(RestaurantList.this, Home.class);
                        categoryIntent.putExtra("RestaurantId", adapter.getRef(position).getKey());
                        startActivity(categoryIntent);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }


}

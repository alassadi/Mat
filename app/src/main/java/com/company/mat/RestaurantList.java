package com.company.mat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.company.mat.Interface.ItemClickListener;
import com.company.mat.Model.Restaurant;
import com.company.mat.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RestaurantList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference restaurantList;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Restaurants");
        setSupportActionBar(toolbar);


        // firebase
        database = FirebaseDatabase.getInstance();
        restaurantList = database.getReference("restaurants");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(RestaurantList.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_r);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

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
        adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(Restaurant.class, R.layout.restaurant_item, RestaurantViewHolder.class, restaurantList) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder, Restaurant model, int position) {
                viewHolder.textRestaurantName.setText(model.getName());
                Picasso.get().load(model.getImageURL()).into(viewHolder.imageView);
                final Restaurant clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // send category id to the food food list activity
                        Intent categoryIntent = new Intent(RestaurantList.this, Home.class);
                        categoryIntent.putExtra("RestaurantId", adapter.getRef(position).child("RestaurantId").getKey());
                        startActivity(categoryIntent);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_r);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent restaurantIntent = new Intent(RestaurantList.this, RestaurantList.class);
            startActivity(restaurantIntent);
        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(RestaurantList.this, Cart.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(RestaurantList.this, LoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_r);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

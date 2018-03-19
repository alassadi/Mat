package com.company.mat;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.company.mat.Fragments.ItemFragment;
import com.company.mat.Fragments.RestaurantBannerFragment;
import com.company.mat.Model.Address;
import com.company.mat.Model.Restaurant;
import com.company.mat.Model.RestaurantOrderListItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class RestaurantAccount extends FragmentActivity implements ItemFragment.OnListFragmentInteractionListener {

    private DatabaseReference dbref;
    private Restaurant restaurant;

    private RestaurantBannerFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_account);


        frag = new RestaurantBannerFragment();
        ItemFragment frag2 = new ItemFragment();

        dbref = FirebaseDatabase.getInstance().getReference()
                .child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurant = dataSnapshot.getValue(Restaurant.class);
                if (restaurant != null) {
                    frag.update(restaurant.getName(), restaurant.getDescription());
                    frag.setImage(restaurant.getImageURL());

                    if (restaurant.getOrders() == null || restaurant.getOrders().isEmpty()) {
                        HashMap<String, String> items = new HashMap<>();
                        items.put("pizza", "5");
                        //restaurant.addOrder(new RestaurantOrderListItem("comments", "address", items, "number", "name", "100"));
                        //FirebaseDatabase.getInstance().getReference().child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(restaurant);
                    }
                } else {
                    restaurant = new Restaurant("Restaurant Name", new Address(), "Restaurant Description");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // log a message
                Log.w(" ", "loadPost:onCancelled", databaseError.toException());
            }
        };
        dbref.addValueEventListener(eventListener);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.RestaurantBannerContainer, frag);
        ft.add(R.id.RestaurantListContainer, frag2);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }


    @Override
    public void onListFragmentInteraction(ListItem item) {
        if (((String) item.getEntry(0)).equalsIgnoreCase("Edit profile")) {
            Intent intent = new Intent(this, EditRestaurantProfile.class);
            intent.putExtra("restaurant", restaurant);
            startActivity(intent);
        } else if (((String) item.getEntry(0)).equalsIgnoreCase("Edit Address")) {
            Intent intent = new Intent(this, RestaurantEditAddress.class);
            intent.putExtra("restaurant", restaurant);
            startActivity(intent);

        } else if (((String) item.getEntry(0)).equalsIgnoreCase("My Orders")) {
            Intent intent = new Intent(this, RestaurantOrderListActivity.class);
            intent.putExtra("restaurant", restaurant);
            startActivity(intent);

            //Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();

        } else if (((String) item.getEntry(0)).equalsIgnoreCase("Edit menu")) {
            Intent intent = new Intent(this, RestaurantEditMenu.class);
            intent.putExtra("menu", restaurant.getMenu());
            startActivity(intent);
        }
    }
}

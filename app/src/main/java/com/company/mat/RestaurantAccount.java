package com.company.mat;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.company.mat.Fragments.ItemFragment;
import com.company.mat.Fragments.RestaurantBannerFragment;


public class RestaurantAccount extends FragmentActivity implements ItemFragment.OnListFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_account);


        RestaurantBannerFragment frag = new RestaurantBannerFragment();
        ItemFragment frag2 = new ItemFragment();


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.RestaurantBannerContainer, frag);
        ft.add(R.id.RestaurantListContainer, frag2);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }


    @Override
    public void onListFragmentInteraction(ListItem item) {
        Toast.makeText(this, "Hello there.", Toast.LENGTH_SHORT).show();

        if (item.getEntry().equalsIgnoreCase("Edit profile")) {
            startActivity(new Intent(this, EditRestaurantProfile.class));
        }
    }
}

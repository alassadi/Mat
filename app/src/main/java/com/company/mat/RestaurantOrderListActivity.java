package com.company.mat;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.company.mat.Fragments.RestaurantOrderListFragment;
import com.company.mat.Interface.ItemClickListener;
import com.company.mat.Model.Category;
import com.company.mat.Model.Restaurant;
import com.company.mat.Model.RestaurantOrderListItem;
import com.company.mat.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantOrderListActivity extends FragmentActivity implements RestaurantOrderListFragment.OnListFragmentInteractionListener {

    private RestaurantOrderListFragment fragment;

    private Restaurant restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_order_list);
        fragment = new RestaurantOrderListFragment();


        if (getIntent().getSerializableExtra("restaurant") != null) {
            Bundle bundle = new Bundle();
            restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
            bundle.putSerializable("restaurant", restaurant);
            fragment.setArguments(bundle);
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.RestaurantOrderListContainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    @Override
    public void onListFragmentInteraction(RestaurantOrderListItem item) {
        Intent intent = new Intent(this, RestaurantOrderItemDetails.class);
        intent.putExtra("item", item);


        startActivity(intent);
    }
}

package com.company.mat.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.mat.Model.Restaurant;
import com.company.mat.Model.RestaurantOrderListItem;
import com.company.mat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RestaurantOrderListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private View view;
    private HashMap<String, RestaurantOrderListItem> orders;
    private Restaurant restaurant;

    public RestaurantOrderListFragment() {
    }

    @SuppressWarnings("unused")
    public static RestaurantOrderListFragment newInstance(int columnCount) {
        return new RestaurantOrderListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurant_order_item_list, container, false);

        restaurant = (Restaurant) getArguments().getSerializable("restaurant");
        if (restaurant != null && restaurant.getOrders() != null) {
            orders = restaurant.getOrders();
        } else {
            orders = new HashMap<>();
            restaurant.setOrders(orders);
            Snackbar.make(container, "Restaurant is null.", Snackbar.LENGTH_SHORT).show();
        }


        // Set the adapter
        loadOrders();
        if (orders == null || orders.isEmpty()) {
            orders = new HashMap<>();


            Snackbar.make(container, "You have no orders pending.", Snackbar.LENGTH_SHORT).show();
        }
        updateUI();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateUI() {
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RestaurantOrderItemRecyclerViewAdapter(restaurant.orderToArrayList(), mListener));
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(RestaurantOrderListItem item);
    }

    private void loadOrders() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("restaurants").child(userID).child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders = new HashMap<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RestaurantOrderListItem order = ds.getValue(RestaurantOrderListItem.class);
                    if (order != null) {
                        orders.put(order.getId(), order);
                        restaurant.setOrders(orders);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

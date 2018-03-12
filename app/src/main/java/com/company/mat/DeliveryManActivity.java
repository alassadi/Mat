package com.company.mat;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class DeliveryManActivity extends AppCompatActivity {

    //private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_man);

        recyclerView=findViewById(R.id.delivery_man_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //getting veriables from database here
        ArrayList<String>customerName=new ArrayList<>();
        customerName.add("Fatih Sayilir");
        customerName.add("Abood Al Asadi");
        customerName.add("Tadas Ivanauskas");

        ArrayList<String>restaurantName=new ArrayList<>();
        restaurantName.add("Vespa");
        restaurantName.add("Bistro Queen");
        restaurantName.add("Jensens Böfhus");

        ArrayList<String>time=new ArrayList<>();
        time.add("14:00:00");
        time.add("14:30:00");
        time.add("14:45:00");

        adapter=new DeliveryManAdapter(getApplicationContext(),customerName,restaurantName,time);
        recyclerView.setAdapter(adapter);

    }
}

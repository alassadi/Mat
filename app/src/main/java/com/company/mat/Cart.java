package com.company.mat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.company.mat.Model.FoodOrder;
import com.company.mat.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reference;

    TextView textViewTotal;
    Button orderButton;

    List<FoodOrder> cart = new ArrayList<>();
    FirebaseRecyclerAdapter<FoodOrder, CartViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("FoodOrders");

        recyclerView = (RecyclerView) findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        textViewTotal = (TextView) findViewById(R.id.total);
        orderButton = (Button) findViewById(R.id.placeOrderButton);

        loadFoodList();
    }

    private void loadFoodList() {
        adapter = new FirebaseRecyclerAdapter<FoodOrder, CartViewHolder>(FoodOrder.class, R.layout.cart_layout, CartViewHolder.class, reference) {
            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, FoodOrder model, int position) {
                viewHolder.cartName.setText(model.getItemName());
                int total = 0;
                for (FoodOrder foodOrder : cart) {
                    total += (Integer.parseInt(foodOrder.getPrice())) * (Integer.parseInt(foodOrder.getQuantity()));
                }
                Locale locale = new Locale("en", "SE");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                textViewTotal.setText(numberFormat.format(total));

            }
        };
        recyclerView.setAdapter(adapter);
    }
}

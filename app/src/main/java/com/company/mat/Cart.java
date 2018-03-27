package com.company.mat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.company.mat.Model.FoodOrder;
import com.company.mat.Model.RestaurantOrderListItem;
import com.company.mat.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference reference;

    TextView textViewTotal;
    Button orderButton;
    FirebaseRecyclerAdapter<FoodOrder, CartViewHolder> adapter;
    List<RestaurantOrderListItem> foodOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("FoodOrders").child(FirebaseAuth.getInstance().getUid());

        recyclerView = (RecyclerView) findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        textViewTotal = (TextView) findViewById(R.id.total);
        orderButton = (Button) findViewById(R.id.placeOrderButton);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        loadFoodList();
    }

    private void showInputDialog() {
        final AlertDialog.Builder alerDialog = new AlertDialog.Builder(Cart.this);
        alerDialog.setTitle("Almost Done!");
        alerDialog.setMessage("Enter Your Address: ");

        LinearLayout linearLayout = new LinearLayout(Cart.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText addressEditText = new EditText(Cart.this);
        addressEditText.setHint("Address");
        linearLayout.addView(addressEditText);

        final EditText commentEditText = new EditText(Cart.this);
        commentEditText.setHint("Comment");
        linearLayout.addView(commentEditText);

        final EditText nameEditText = new EditText(Cart.this);
        nameEditText.setHint("Name");
        linearLayout.addView(nameEditText);

        final EditText phoneEditText = new EditText(Cart.this);
        phoneEditText.setHint("PhoneNumber");
        phoneEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(phoneEditText);

        alerDialog.setView(linearLayout);
        alerDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //hashMap.put(item.getId(), item);

        alerDialog.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference cartRef = rootRef.child("FoodOrders").child(FirebaseAuth.getInstance().getUid());

                ValueEventListener eventListener = new ValueEventListener() {
                    Locale locale = new Locale("en", "SE");
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String restid;

                        Log.e("ds",dataSnapshot.toString());

                        for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                            // String.valueOf(commentEditText.getText()),String.valueOf(adcartRefdressEditText.getText()), hashMap
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(String.valueOf(dSnapshot.child("itemName").getValue()), String.valueOf(dSnapshot.child("quantity").getValue()));
                            int sum = Integer.parseInt(String.valueOf(dSnapshot.child("price").getValue())) * Integer.parseInt(String.valueOf(dSnapshot.child("quantity").getValue()));
                            RestaurantOrderListItem restaurantOrderListItem = new RestaurantOrderListItem(String.valueOf(commentEditText.getText()), String.valueOf(addressEditText.getText()), hashMap, Integer.parseInt(phoneEditText.getText().toString()), String.valueOf(nameEditText.getText()), numberFormat.format(sum) + "");
                            restaurantOrderListItem.setTime(Calendar.getInstance().getTime().toString());
                            restid = String.valueOf(dSnapshot.child("restaurantId").getValue());
                            rootRef.child("restaurants").child(restid).child("orders").child(restaurantOrderListItem.getId()).setValue(restaurantOrderListItem);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                cartRef.addListenerForSingleValueEvent(eventListener);

                Toast.makeText(getApplicationContext(), "Your Order is Sent", Toast.LENGTH_LONG).show();
                reference.removeValue();

                finish();
            }
        });
        alerDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alerDialog.show();

    }

    private void loadFoodList() {
        adapter = new FirebaseRecyclerAdapter<FoodOrder, CartViewHolder>(FoodOrder.class, R.layout.cart_layout, CartViewHolder.class, reference) {
            @Override
            protected void populateViewHolder(CartViewHolder viewHolder, FoodOrder model, int position) {
                if (model==null){return;}
                viewHolder.cartName.setText(model.getItemName());
                try {
                    int price = (Integer.parseInt(model.getPrice())) * (Integer.parseInt(model.getQuantity()));
                    viewHolder.cartPrice.setText(price + "");
                }catch (Exception e){
                    Log.e("price","price is empty");
                }
                //a drawable to show the quantity of each item
                TextDrawable textDrawable = TextDrawable.builder().beginConfig()
                        .textColor(Color.WHITE)
                        .bold()
                        .endConfig().buildRoundRect(model.getQuantity(), Color.RED, 10);
                viewHolder.cartCount.setImageDrawable(textDrawable);

                //counting the total cost
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference cartRef = rootRef.child("FoodOrders");
                ValueEventListener eventListener = new ValueEventListener() {
                    Locale locale = new Locale("en", "SE");
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int sum = 0;
                        for (DataSnapshot dSnapshot : dataSnapshot.child(FirebaseAuth.getInstance().getUid()).getChildren()) {
                            try {
                                sum = sum + (Integer.parseInt(dSnapshot.child("price").getValue(String.class).trim()) * Integer.parseInt(dSnapshot.child("quantity").getValue(String.class).trim()));
                            } catch (Exception e) {
                                Log.e("price", "price is empty");
                            }
                        }
                        textViewTotal.setText(numberFormat.format(sum) + "");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                cartRef.addListenerForSingleValueEvent(eventListener);
            }
        };
        recyclerView.setAdapter(adapter);
    }
}

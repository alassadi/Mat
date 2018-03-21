package com.company.mat;

import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.company.mat.Model.RestaurantOrderListItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RestaurantOrderItemDetails extends AppCompatActivity {

    private Button save, deliver;
    private TextView tvID, tvDesc, phone, tvitems, time, price;
    private RestaurantOrderListItem item;
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_order_item_details);

        save = findViewById(R.id.restaurantOrderSaveButton);
        deliver = findViewById(R.id.restaurantOrderDeliverButton);
        tvDesc = findViewById(R.id.tvOrderDescription);
        tvID = findViewById(R.id.tvID);
        tvitems = findViewById(R.id.tvItems);
        time = findViewById(R.id.orderTimePick);
        price = findViewById(R.id.tvOrderPrice);
        phone = findViewById(R.id.orderPhone);


        if (getIntent().getSerializableExtra("item") != null) {
            item = (RestaurantOrderListItem) getIntent().getSerializableExtra("item");
            tvID.append("\n" + item.getId());
            tvDesc.append("\n" + item.getComments());
            price.append("\n" + item.getPrice());
            phone.append("\n" + item.getpNumber());
            if (item.getTime() != null && !item.getTime().isEmpty()) {
                time.setText(item.getTime());
            }
            StringBuilder itemString = new StringBuilder();
            for (String s : item.getItems().keySet()) {
                itemString.append(item.getItems().get(s)).append(" ").append(s).append("\n");
            }
            tvitems.setText(itemString.toString());
            restaurantName = getIntent().getStringExtra("restaurant");
        } else {
            Snackbar.make(save, "Empty", Snackbar.LENGTH_LONG).show();
        }

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(save.getContext());
                final TimePicker timePicker = new TimePicker(save.getContext());
                alert.setTitle("Choose time");
                alert.setMessage("Choose a time when the order will be finished");
                alert.setView(timePicker);


                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                        String hour = String.valueOf(i);
                        String minute = String.valueOf(i1);
                        if (hour.length() == 1) {
                            hour = "0" + hour;
                        }
                        if (minute.length() == 1) {
                            minute = "0" + minute;
                        }
                        time.setText(String.format(getString(R.string.timePlaceholder), hour, minute));
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //canceled
                    }
                });

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item.setTime(time.getText().toString());
                    }
                });
                alert.show();
            }
        });

        save.setOnClickListener(getSaveButtonListener());
        deliver.setOnClickListener(getDeliveryButtonListener());

    }

    private View.OnClickListener getSaveButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(item.getId(), item);
                FirebaseDatabase.getInstance().getReference().child("restaurants").child(uid).child("orders").updateChildren(hashMap);
                onBackPressed();
            }
        };
    }

    private View.OnClickListener getDeliveryButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> delivery = new HashMap<>();
                delivery.put("customerAddress", item.getAddress());
                delivery.put("customerName", item.getName());
                delivery.put("deliveryStatus", "Free");
                //delivery.put("orderId", item.getId());
                delivery.put("phoneNumber",item.getpNumber() );
                delivery.put("restaurantName", restaurantName);
                FirebaseDatabase.getInstance().getReference().child("orders").child(item.getId()).setValue(delivery);

                // to delete dispatched order
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(item.getId(), null);
                FirebaseDatabase.getInstance().getReference().child("restaurants").child(uid).child("orders").updateChildren(hashMap);
            }
        };
    }

}

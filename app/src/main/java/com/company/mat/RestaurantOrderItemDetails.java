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
    private TextView tvID, tvDesc, tvitems, time;
    private RestaurantOrderListItem item;

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

        if (getIntent().getSerializableExtra("item") != null) {
            item = (RestaurantOrderListItem) getIntent().getSerializableExtra("item");
            tvID.setText(item.getId());
            tvDesc.setText(item.getComments());
            String itemString = "";
            for (String s : item.getItems().keySet()) {
                itemString = itemString + item.getItems().get(s) + " " + s + "\n";
            }
            tvitems.setText(itemString);
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
                        time.setText(i + ":" + i1);
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

                    }
                });
                alert.show();
            }
        });

        save.setOnClickListener(getSaveButtonListener());


    }

    private View.OnClickListener getSaveButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(item.getId(), item);
                FirebaseDatabase.getInstance().getReference().child("restaurants").child(uid).child("orders").updateChildren(hashMap);
            }
        };
    }
}

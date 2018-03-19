package com.company.mat;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.company.mat.Fragments.MyDeliveryAdapter;
import com.company.mat.Model.CustonItemClickListener;
import com.company.mat.Model.DeliveryItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyDeliveryActivity extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;


    private List<DeliveryItem>mydeliveries=new ArrayList<DeliveryItem>();
    private ArrayList<String>myKeys=new ArrayList<>();
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_delivery);

        //toolbar
        Toolbar toolbar=findViewById(R.id.deliver_man_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Deliveries");





        //Database connection
        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference dref=database.getReference("DeliveryList");
        //get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser=firebaseUser.getUid();


        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //if statement to check current user name and list his orders
                //add user id in to delivery table to so each user can see their own deliverylist

                    DeliveryItem addedItem=dataSnapshot.getValue(DeliveryItem.class);
                    String key=dataSnapshot.getKey();

                        myKeys.add(key);
                        mydeliveries.add(addedItem);
                        adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                DeliveryItem changed=dataSnapshot.getValue(DeliveryItem.class);
                String key=dataSnapshot.getKey();

                int index=myKeys.indexOf(key);
                mydeliveries.set(index,changed);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                DeliveryItem removed=dataSnapshot.getValue(DeliveryItem.class);
                String key=dataSnapshot.getKey();
                int index=myKeys.indexOf(key);
                mydeliveries.remove(index);
                myKeys.remove(key);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(" ", "loadPost:onCancelled", databaseError.toException());
            }
        });




        //Create Toolbar
        floatingButton();

        recyclerView=findViewById(R.id.mydelivery_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(MyDeliveryActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        adapter=new MyDeliveryAdapter(this,mydeliveries, new CustonItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                // do what ever you want to do with it
                Intent intent=new Intent();
                intent.setClass(MyDeliveryActivity.this,DeliveryDetailActivity.class);

                //pass order data to DeliveryManActivty
                Bundle bundle=new Bundle();

                bundle.putString("customerName",mydeliveries.get(position).getCustomerName());
                bundle.putString("customerAddress",mydeliveries.get(position).getCustomerAddress());
                bundle.putString("phoneNumber", String.valueOf(mydeliveries.get(position).getPhoneNumber()));
                bundle.putString("restaurantName",mydeliveries.get(position).getRestaurantName());
                bundle.putString("deliveryStatus",String.valueOf(mydeliveries.get(position).getDeliveryStatus()));
                bundle.putString("itemkey", myKeys.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(adapter);


    }
    public void floatingButton(){
        FloatingActionButton fab = findViewById(R.id.myfloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(MyDeliveryActivity.this,DeliveryManActivity.class);

                //pass order data to DeliveryManActivty
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delivery_toolbar, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...

                return true;
/*
            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}

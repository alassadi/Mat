package com.company.mat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.company.mat.Fragments.DeliveryDialogFragment;
import com.company.mat.Fragments.DeliveryManAdapter;
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

public class DeliveryManActivity extends AppCompatActivity{


    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;


    private List<DeliveryItem>orders=new ArrayList<DeliveryItem>();
    private ArrayList<String>myKeys=new ArrayList<>();

    String cusName,cusAddress,delStatus,resName,key,userId;
    long phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_man);

        //create toolbar
        Toolbar toolbar=findViewById(R.id.deliver_man_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mat Food App");



        //Database connection
        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference dref=database.getReference("orders");

        final MainActivity mainActivity=new MainActivity();


        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DeliveryItem addedItem=dataSnapshot.getValue(DeliveryItem.class);
                String key=dataSnapshot.getKey();
                myKeys.add(key);

                    orders.add(addedItem);
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                DeliveryItem changed=dataSnapshot.getValue(DeliveryItem.class);
                String key=dataSnapshot.getKey();

                int index=myKeys.indexOf(key);
                orders.set(index,changed);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                DeliveryItem removed=dataSnapshot.getValue(DeliveryItem.class);
                String key=dataSnapshot.getKey();
                int index=myKeys.indexOf(key);
                orders.remove(index);
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

        recyclerView=findViewById(R.id.delivery_man_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(DeliveryManActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        adapter=new DeliveryManAdapter(this,orders, new CustonItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

               DeliveryDialogFragment dialogFragment=DeliveryDialogFragment.deliveryDialogFragment("DELIVERY DETAILS",orders.get(position).toString());
               dialogFragment.show(getSupportFragmentManager(),"dialog");

               resName=orders.get(position).getRestaurantName();
               phoneNumber= orders.get(position).getPhoneNumber();
               cusName=orders.get(position).getCustomerName();
               cusAddress=orders.get(position).getCustomerAddress();
               delStatus=orders.get(position).getDeliveryStatus();
               key=myKeys.get(position);

            }
        });
        recyclerView.setAdapter(adapter);


    }
    public void floatingButton(){
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(DeliveryManActivity.this,MyDeliveryActivity.class);

                //pass order data to DeliveryManActivty
                startActivity(intent);
            }
        });
    }

   public void onClickAccept(){
        //Toast.makeText(getApplicationContext(),"On click Accept",Toast.LENGTH_LONG).show();

       //current user is childkey for deliveryItem in order to display just users oreder in my order
       FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       if (firebaseUser != null) {
           userId = firebaseUser.getUid();
       }

        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference dref=database.getReference("DeliveryList").child(userId);


        DeliveryItem item=new DeliveryItem(cusAddress,cusName,delStatus,phoneNumber,resName);
        dref.child(key).setValue(item);
        removeFromList(key);

    }
    private void removeFromList(String key){
        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference dref=database.getReference("orders");
        dref.child(key).removeValue();

    }
    public void onClickCancel(){
        Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_LONG).show();
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
package com.company.mat;

import android.app.Notification;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * for testing purposes
         * you can login with tadas@fatapp.com
         * 123123
         * */
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("M0fSGuSgK5bWhY4iqoDgsl5Wv8j1")) {
            startActivity(new Intent(this, RestaurantAccount.class));

        }

    }
    public void showNotification(View view){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("New Delivery");
        builder.setContentText("New Delivery available");
        builder.setAutoCancel(true);
        builder.setPriority(Notification.DEFAULT_VIBRATE);


    }
}

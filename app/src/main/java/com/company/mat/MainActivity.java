package com.company.mat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}

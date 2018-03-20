package com.company.mat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private TextView statusTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.field_email);
        password = findViewById(R.id.field_password);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        statusTest = findViewById(R.id.status);

        mAuth = FirebaseAuth.getInstance();

        /* this line is to prevent the background image from cropping
           or resizing when the keyboard appears.
        */
        getWindow().setBackgroundDrawableResource(R.drawable.loginimage);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn(String email, String password) {
        if (!checkInput()) {
            return;
        }

        showProgressDialog();

        // here we sign in with email and password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            statusTest.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void createAccount(String email, String password) {
        if (!checkInput()) {
            return;
        }

        showProgressDialog();

        // create a user from email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.email_sign_in_button) {
            signIn(email.getText().toString(), password.getText().toString());
        } else if (i == R.id.email_create_account_button) {
            createAccount(email.getText().toString(), password.getText().toString());
        }

    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        // here transition to the new activitiy with the user passed

        // add sign out

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, RestaurantList.class);
            startActivity(intent);
        }
    }

    private boolean checkInput() {
        boolean valid = true;

        String email = this.email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.email.setError("Required.");
            valid = false;
        } else {
            this.email.setError(null);
        }

        String password = this.password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            this.password.setError("Required.");
            valid = false;
        } else {
            this.password.setError(null);
        }

        return valid;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}






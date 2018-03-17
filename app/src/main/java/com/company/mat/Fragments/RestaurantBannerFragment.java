package com.company.mat.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.mat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by ivana on 3/10/2018.
 */

public class RestaurantBannerFragment extends Fragment {

    private ImageView image;

    private TextView name, description;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstances) {
        View v = inflater.inflate(R.layout.fragment_restaurant_banner, container, false);
        name = v.findViewById(R.id.tvRestaurantName);
        description = v.findViewById(R.id.tvRestaurantDescription);
        image = v.findViewById(R.id.profilePicture);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Profile Picture");
                alert.setMessage("Enter URL for your picture.");
                final EditText editText = new EditText(getContext());
                alert.setView(editText);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setImage(editText.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("restaurants").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageURL").setValue(editText.getText().toString());

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });


        return v;
    }


    public void update(String name, String desc) {
        this.name.setText(name);
        this.description.setText(desc);
    }

    public void setImage(String url) {
        Picasso.get().load(url).into(image);
    }

}

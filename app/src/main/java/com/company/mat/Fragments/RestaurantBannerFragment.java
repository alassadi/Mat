package com.company.mat.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.company.mat.Model.Restaurant;
import com.company.mat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ivana on 3/10/2018.
 */

public class RestaurantBannerFragment extends Fragment {

    private static final int SELECT_PHOTO = 100;
    private ContentResolver contentResolver;
    private ImageButton imageButton;

    private TextView name, description;
    private DatabaseReference dbref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        View v = inflater.inflate(R.layout.fragment_restaurant_banner, container, false);
        name = v.findViewById(R.id.tvRestaurantName);
        description = v.findViewById(R.id.tvRestaurantDescription);
        imageButton = v.findViewById(R.id.profilePicture);

        contentResolver = v.getContext().getContentResolver();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        dbref = FirebaseDatabase.getInstance().getReference()
                .child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                if (restaurant != null) {
                    name.setText(restaurant.getName());
                    description.setText(restaurant.getDescription());
//                    if (restaurant.getImage()!=null){
//                        imageButton.setImageBitmap(restaurant.getImage());
//                    }
                } else {
                    Toast.makeText(getContext(), "Restaurant is null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // log a message
                Log.w(" ", "loadPost:onCancelled", databaseError.toException());
            }
        };
        dbref.addValueEventListener(postListener);
        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        Bitmap image = decodeUri(selectedImage);

                        imageButton.setImageBitmap(image);
                        dbref = FirebaseDatabase.getInstance().getReference();
                        dbref.child("restaurants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("image").setValue(image);
                    } catch (Exception e) {
                        Log.e("Picture selection", e.getMessage());
                    }
                }
        }
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 96;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o2);

    }

}

package com.company.mat.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.mat.Interface.ItemClickListener;
import com.company.mat.R;

/**
 * Created by Abood on 3/17/2018.
 */

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textRestaurantName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public RestaurantViewHolder(View itemView) {
        super(itemView);

        textRestaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
        imageView = (ImageView) itemView.findViewById(R.id.restaurant_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

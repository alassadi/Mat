package com.company.mat.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.mat.Interface.ItemClickListener;
import com.company.mat.R;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartName, cartPrice;
    public ImageView cartCount;
    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);
        cartName = (TextView) itemView.findViewById(R.id.cart_item_name);
        cartPrice = (TextView) itemView.findViewById(R.id.cart_item_price);
        cartCount = (ImageView) itemView.findViewById(R.id.cart_item_count);
    }

    public void setCartName(TextView cartName) {
        this.cartName = cartName;
    }

    @Override
    public void onClick(View view) {

    }


}



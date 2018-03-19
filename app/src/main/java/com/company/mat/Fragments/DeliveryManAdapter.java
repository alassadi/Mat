package com.company.mat.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.mat.DeliveryManActivity;
import com.company.mat.Model.CustonItemClickListener;
import com.company.mat.Model.DeliveryItem;
import com.company.mat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fatih on 2018-03-12.
 */

public class DeliveryManAdapter extends RecyclerView.Adapter<DeliveryManAdapter.Viewholder>{

    Context context;
    List<DeliveryItem>orders;
    CustonItemClickListener listener;

    //constructer


    public DeliveryManAdapter(Context context,List<DeliveryItem> orders, CustonItemClickListener listener) {
        this.context=context;
        this.orders = orders;
        this.listener = listener;
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView resName;
        public TextView status;
        public RelativeLayout singelRow;

        public Viewholder(final View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.textView_customer_name);
            resName=itemView.findViewById(R.id.textView_rest_name);
            status=itemView.findViewById(R.id.textView_status);
            singelRow= itemView.findViewById(R.id.singel_Row);

        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_row_list,parent,false);
        final Viewholder viewholder=new Viewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,viewholder.getLayoutPosition());

            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        DeliveryItem deliveryItem=orders.get(position);
        holder.name.setText(deliveryItem.getCustomerName());
        holder.resName.setText(deliveryItem.getRestaurantName());
        holder.status.setText(deliveryItem.getDeliveryStatus());
        holder.singelRow.setTag(holder);



    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}
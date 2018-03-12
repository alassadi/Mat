package com.company.mat;

import android.content.Context;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fatih on 2018-03-12.
 */

public class DeliveryManAdapter extends RecyclerView.Adapter<DeliveryManAdapter.Viewholder>{

    Context ctx;
    public ArrayList<String>customerName;
    public ArrayList<String>restaurantName;
    public ArrayList<String>timedTexts;

    //constructer


    public DeliveryManAdapter(Context ctx, ArrayList<String> customerName, ArrayList<String> restaurantName, ArrayList<String> timedTexts) {
        this.ctx = ctx;
        this.customerName = customerName;
        this.restaurantName = restaurantName;
        this.timedTexts = timedTexts;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView resName;
        private TextView time;
        public RelativeLayout singelRow;

        public Viewholder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.textView_customer_name);
            resName=itemView.findViewById(R.id.textView_rest_name);
            time=itemView.findViewById(R.id.textView_time);
            singelRow= itemView.findViewById(R.id.singel_Row);
        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_row_list,parent,false);
        Viewholder viewholder=new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        holder.name.setText(customerName.get(position));
        holder.resName.setText(restaurantName.get(position));
        holder.time.setText(timedTexts.get(position));

        holder.singelRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create new fragment here and pass the variables
                //to the fragment
                /*
                Viewholder viewholder= (Viewholder) v.getTag();
                int position=viewholder.getLayoutPosition();
                */
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerName.size();
    }

}
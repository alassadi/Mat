package com.company.mat.Fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.mat.Fragments.RestaurantOrderListFragment.OnListFragmentInteractionListener;
import com.company.mat.Model.RestaurantOrderListItem;
import com.company.mat.R;

import java.util.List;


public class RestaurantOrderItemRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantOrderItemRecyclerViewAdapter.ViewHolder> {

    private final List<RestaurantOrderListItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public RestaurantOrderItemRecyclerViewAdapter(List<RestaurantOrderListItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public RestaurantOrderListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.RestaurantOrderId);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
